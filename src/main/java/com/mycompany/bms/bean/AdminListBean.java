package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@RequestScoped
public class AdminListBean {

    @Inject
    private AdminService adminService;

    private List<Admin> admins;
    private Long adminIdToDelete;

    // Load the list of admins when the bean is initialized
    @PostConstruct
    public void init() {
        loadAdmins();
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public Long getAdminIdToDelete() {
        return adminIdToDelete;
    }

    public void setAdminIdToDelete(Long adminIdToDelete) {
        this.adminIdToDelete = adminIdToDelete;
    }

    // Load the list of admins from the service
    private void loadAdmins() {
        admins = adminService.getAllAdmins();
    }

    // Prepare for editing an admin
    public String prepareEdit(Admin admin) {
        // Logic to prepare the admin for editing
        // This could involve setting a selected admin in a separate bean or redirecting to an edit page
        return "updateAdmin?faces-redirect=true&id=" + admin.getId();
    }

    // Delete an admin
    public void deleteAdmin(Long id) {
        try {
            adminService.deleteAdmin(id);
            loadAdmins(); // Refresh the list after deletion
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete admin"));
            e.printStackTrace();
        }
    }
}
