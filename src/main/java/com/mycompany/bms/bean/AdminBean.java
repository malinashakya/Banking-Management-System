package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Admin admin = new Admin();

    @EJB
    private AdminService adminService;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void saveAdmin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            System.out.println("Attempting to save admin: " + admin.getUsername());
            adminService.saveAdmin(admin);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            System.out.println("Admin saved successfully: " + admin.getUsername());
            admin = new Admin(); // Clear form after submission
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save admin"));
            e.printStackTrace();
        }
    }
}