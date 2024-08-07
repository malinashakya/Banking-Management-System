package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ViewScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Admin admin = new Admin();

    private final AdminService adminService = new AdminService();

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void saveAdmin() {
        try {
            adminService.save(admin);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            admin = new Admin(); // Clear form after submission
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save admin"));
            e.printStackTrace();
        }
    }
}
