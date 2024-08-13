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

    private List<Admin> admins;  // List of all admins
    private Admin selectedAdmin; // Admin selected for editing
    private Admin newAdmin;      // New admin for creation
    
    @Inject
    private AdminService adminService;

    @PostConstruct
    public void init() {
        admins = adminService.getAllAdmins(); // Load all admins when bean is initialized
        newAdmin = new Admin(); // Initialize newAdmin
    }

    // Getters and setters
    public List<Admin> getAdmins() {
        return admins;
    }

    public Admin getSelectedAdmin() {
        if (selectedAdmin == null) {
            selectedAdmin = new Admin(); // Initialize selectedAdmin if null
        }
        return selectedAdmin;
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }
    
        public Admin getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(Admin newAdmin) {
        this.newAdmin = newAdmin;
    }

    // Prepare the selected admin for editing
    public void prepareEdit(Admin admin) {
        this.selectedAdmin = adminService.getAdminById(admin.getId()); // Fetch fresh admin data from DB
    }

    // Update the admin information
    public void updateAdmin() {
        try {
            adminService.updateAdmin(selectedAdmin); // Save the updated admin
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin updated successfully", null));
            admins = adminService.getAllAdmins(); // Refresh the admin list after update
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating admin", null));
            e.printStackTrace();
        }
    }

    // Delete an admin
    public void deleteAdmin(Admin admin) {
        try {
            adminService.deleteAdmin(admin.getId()); // Delete the admin
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin deleted successfully", null));
            admins = adminService.getAllAdmins(); // Refresh the admin list after deletion
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error deleting admin", null));
            e.printStackTrace();
        }
    }
    
     // Create a new admin
    public void createAdmin() {
        try {
            // Validate newAdmin fields
            if (newAdmin.getUsername() == null || newAdmin.getUsername().isEmpty() ||
                newAdmin.getName() == null || newAdmin.getName().isEmpty() ||
                newAdmin.getPassword() == null || newAdmin.getPassword().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please fill all required fields", null));
                return;
            }
            adminService.saveAdmin(newAdmin); // Save the new admin
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin created successfully", null));
            admins = adminService.getAllAdmins(); // Refresh the admin list after creation
            newAdmin = new Admin(); // Clear the form
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating admin", null));
            e.printStackTrace();
        }
    }
}