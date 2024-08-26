package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.repository.AdminRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.HashMap;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AdminRepository adminRepository;

    private Admin selectedAdmin;
    private boolean editMode = false;
    private GenericLazyDataModel<Admin> lazyDataModel;
    private int pageSize = 5;

    @PostConstruct
    public void init() {
        if (selectedAdmin == null) {
            selectedAdmin = new Admin();
        }

        lazyDataModel = new GenericLazyDataModel<>(adminRepository, Admin.class);

    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                adminRepository.update(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            } else {
                adminRepository.save(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            }
            selectedAdmin = new Admin();
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
        this.selectedAdmin = entity;
        this.editMode = true;
    }

    public void prepareNewEntity() {
        this.selectedAdmin = new Admin();
        this.editMode = false;
    }

    public Admin getSelectedAdmin() {
        if (selectedAdmin == null) {
            selectedAdmin = new Admin();
        }
        return selectedAdmin;
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public GenericLazyDataModel<Admin> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(GenericLazyDataModel<Admin> lazyDataModel) {
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
