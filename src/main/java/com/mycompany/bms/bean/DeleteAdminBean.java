/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.bean;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

@Named
@RequestScoped
public class DeleteAdminBean {
    
    @Inject
    private AdminService adminService;

    private Long adminId;

    // Getter and setter for adminId
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    // Method to delete admin
    public void deleteAdmin() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Admin admin = adminService.getAdminById(adminId);
            if (admin != null) {
                adminService.deleteAdmin(adminId);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully."));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Admin not found."));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occurred while deleting the admin."));
            e.printStackTrace();
        }
    }
}
