package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@Named("adminBean")
@RequestScoped
public class AdminBean {
    private static final long serialVersionUID = 1L;

    private List<Admin> admins;
    private Admin admin = new Admin();

    @EJB
    private AdminService adminService;

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
            e.printStackTrace();
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

    public void redirectToAdminList() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminList.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void prepareEdit(Admin admin) {
        this.admin = admin; // Set the admin to be edited
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("AdminEdit.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin() {
        try {
            // Check if password was modified and handle accordingly
            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                String salt = adminService.generateSalt();
                String hashedPassword = adminService.hashPassword(admin.getPassword(), salt);
                admin.setPassword(hashedPassword + ":" + salt);
            }
            adminService.updateAdmin(admin);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin updated successfully", null));
            // Redirect back to Admin list page
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminList.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating admin", null));
            e.printStackTrace();
        }
    }
}
