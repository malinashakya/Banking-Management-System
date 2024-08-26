package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AdminRepository adminRepository;

    private Admin selectedEntity;
    private boolean editMode = false;
    private LazyDataModel<Admin> lazyDataModel;
    private int pageSize = 5;

    @PostConstruct
    public void init() {
        if (selectedEntity == null) {
            selectedEntity = new Admin();
        }

        lazyDataModel = new LazyDataModel<Admin>() {
            @Override
            public List<Admin> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                List<Admin> admins = adminRepository.getAdmins(first, pageSize, filterBy);
                this.setRowCount(adminRepository.countAdmins(filterBy));
                return admins;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return adminRepository.countAdmins(filterBy);
            }
        };
    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                adminRepository.update(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            } else {
                adminRepository.save(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            }
            selectedEntity = new Admin();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update admin"));
        }
    }

    public void deleteEntity(Admin entity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            adminRepository.delete(entity.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete admin"));
        }
    }

    public void prepareEditEntity(Admin entity) {
        this.selectedEntity = entity;
        this.editMode = true;
    }

    public void prepareNewEntity() {
        this.selectedEntity = new Admin();
        this.editMode = false;
    }

    public Admin getSelectedEntity() {
        if (selectedEntity == null) {
            selectedEntity = new Admin();
        }
        return selectedEntity;
    }

    public void setSelectedEntity(Admin selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public LazyDataModel<Admin> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Admin> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        lazyDataModel.setRowCount(adminRepository.countAdmins(new HashMap<>()));
    }
}
