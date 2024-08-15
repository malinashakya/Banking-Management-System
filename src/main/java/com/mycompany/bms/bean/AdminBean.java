package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Admin selectedAdmin; // Admin selected for editing

    private List<Admin> admins;
    private boolean editMode = false;

    @Inject
    private AdminRepository adminRepository;

    @PostConstruct
    public void init() {
        try {
            admins = adminRepository.getAll(); // Load all admins when bean is initialized
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    public Admin getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void saveOrUpdateAdmin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                adminRepository.update(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            } else {
                adminRepository.save(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            }

            admins = adminRepository.getAll(); //getAll Refresh the admin list
            selectedAdmin = new Admin(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Admin already exists"));
        }
    }

    public void deleteAdmin(Admin admin) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            adminRepository.delete(admin.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully"));
            admins = adminRepository.getAll();//AdminsgetAllsh the admin list
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete admin"));
        }
    }

    public void prepareEditAdmin(Admin admin) {
        this.selectedAdmin = admin;
        this.editMode = true;
    }

    public void prepareNewAdmin() {
        this.selectedAdmin = new Admin();
        this.editMode = false;
    }
}
