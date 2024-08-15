///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.bms.bean;
//
//import com.mycompany.bms.model.Customer;
//import com.mycompany.bms.service.CustomerService;
//import java.io.IOException;
//import javax.enterprise.context.RequestScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.inject.Inject;
//import javax.inject.Named;
//
///**
// *
// * @author malina
// */
//@Named
//@RequestScoped
//public class LoginCustomerBean {
//
//    @Inject
//    private CustomerService customerService;
//
//    private String username;
//    private String password;
//
//    // Getters and setters
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
//      // Login method
//   // Login method
//    public String login() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        try {
//Customer customer = customerService.authenticateCustomer(username, password);
//            if (customer != null) {
//                facesContext.getExternalContext().getSessionMap().put("loggedInAdmin", customer);
//                // Redirect to the Admin dashboard
//                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Customer/CustomerDashboard.xhtml");
//                return null; // Prevent further execution
//            } else {
//                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Please try again."));
//                return null;
//            }
//        } catch (IOException e) {
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "An error occurred during login"));
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}