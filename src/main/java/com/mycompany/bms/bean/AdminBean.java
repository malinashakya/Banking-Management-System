package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
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
    private AdminService adminService;

    @PostConstruct
    public void init() {
        admins = adminService.getAllAdmins(); // Load all admins when bean is initialized
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
                adminService.updateAdmin(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            } else {
                adminService.saveAdmin(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            }

            admins = adminService.getAllAdmins(); // Refresh the admin list
            selectedAdmin = new Admin(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save or update admin"));
        }
    }

    public void deleteAdmin(Admin admin) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            adminService.deleteAdmin(admin.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully"));
            admins = adminService.getAllAdmins(); // Refresh the admin list
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
