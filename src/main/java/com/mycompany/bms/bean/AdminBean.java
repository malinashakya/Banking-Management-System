package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@Named
@RequestScoped
public class AdminBean {
    private static final long serialVersionUID = 1L;

    private List<Admin> admins;

    @EJB
    private AdminService adminService;

    private Admin admin = new Admin();
    
    @PostConstruct
    public void init() {
        admins = adminService.getAllAdmins();
    }

    // Getters and setters
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public List<Admin> getAdmins() {
        return admins;
    }

    public void saveAdmin() {
        try {
            adminService.saveAdmin(admin);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin saved successfully", null));
            admin = new Admin(); // Clear form after saving
            admins = adminService.getAllAdmins(); // Refresh the admin list
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving admin", null));
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
            e.printStackTrace();
        }
    }

    public void prepareEditAdmin(Admin admin) {
        this.admin = admin; // Load the admin for editing
    }

    public void updateAdmin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            adminService.updateAdmin(admin); // Update the admin in the database
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            admins = adminService.getAllAdmins(); // Refresh the admin list
            admin = new Admin(); // Clear form after update
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update admin"));
            e.printStackTrace();
        }
    }
}
    