//package com.mycompany.bms.bean;
//
//import com.mycompany.bms.model.Admin;
//import com.mycompany.bms.service.AdminService;
//import javax.faces.context.FacesContext;
//import javax.faces.application.FacesMessage;
//import javax.inject.Inject;
//import javax.inject.Named;
//import javax.faces.bean.RequestScoped; 
//import java.io.Serializable;
//
//@Named
//@RequestScoped
//public class LoginBean implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private String username;
//    private String password;
//
//    @Inject
//    private AdminService adminService;
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String login() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Admin admin = adminService.getAll().stream()
//            .filter(a -> a.getUsername().equals(username))
//            .findFirst()
//            .orElse(null);
//
//        if (admin != null && admin.getPassword().equals(password)) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful"));
//            return "AdminDashboard?faces-redirect=true";
//        } else {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Please try again."));
//            return null;
//        }
//    }
//}
