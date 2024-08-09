package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    @EJB
    private AdminService adminService;

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
    public void login() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    try {
        Admin admin = adminService.getAdminByUsername(username);
        if (admin != null && admin.checkPassword(password)) {
            facesContext.getExternalContext().getSessionMap().put("loggedInAdmin", admin);
            // Redirect to the Admin dashboard using a relative path
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Admin/AdminDashboard.xhtml");
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password"));
        }
    } catch (Exception e) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "An error occurred during login"));
        e.printStackTrace();
    }
}
}
