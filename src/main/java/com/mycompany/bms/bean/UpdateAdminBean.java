package com.mycompany.bms.bean;


import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class UpdateAdminBean {

    private Long adminId;
    private String username;
    private String password;
    private Admin admin;

    @Inject
    private AdminService adminService;

    @PostConstruct
    public void init() {
        admin = new Admin();
    }

    public void loadAdmin() {
        if (adminId != null) {
            admin = adminService.getAdminById(adminId);
            if (admin != null) {
                username = admin.getUsername();
//                password = "";  // Don't load the password
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Admin not found", "No admin found with the provided ID"));
            }
        }
    }

    public void updateAdmin() {
        if (admin != null) {
            admin.setUsername(username);
            admin.setPassword(password);
            System.out.println("Pasword:"+password);
                    
            adminService.updateAdmin(admin);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Admin updated", "Admin details updated successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Admin details could not be updated"));
        }
    }

    // Getters and setters for adminId, username, password
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}