package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

@Named
@RequestScoped
public class LoginBean {

    @Inject
    private AdminService adminService;

    private String username;
    private String password;

    // Getters and setters
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

      // Login method
   // Login method
    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Admin admin = adminService.authenticate(username, password);
            if (admin != null) {
                facesContext.getExternalContext().getSessionMap().put("loggedInAdmin", admin);
                // Redirect to the Admin dashboard
                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Admin/AdminDashboard.xhtml");
                return null; // Prevent further execution
            } else {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Please try again."));
                return null;
            }
        } catch (IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "An error occurred during login"));
            e.printStackTrace();
            return null;
        }
    }

}
