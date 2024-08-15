package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;


@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Admin selectedAdmin; // Admin selected for editing

    private List<Admin> admins;
    private boolean editMode = false;

    //For Lazy Table
      private LazyDataModel<Admin> lazyAdmins;
      
      
    @Inject
    private AdminRepository adminRepository;

    @PostConstruct
    public void init() {
       try{
           //For lazyTable
             lazyAdmins = new LazyDataModel<Admin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return adminRepository.countAdmins(filterBy); 
            }

            @Override
            public List<Admin> load(int first, int pageSize, 
                    Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                List<Admin> admins = adminRepository.getAdmins(first, pageSize); // Add pagination support in UserRepository
                this.setRowCount(adminRepository.countAdmins(filterBy)); // Count the total number of records
                return admins;
            }

        };
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
      
    }

    // Getters and setters
    public Admin getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void saveOrUpdateAdmin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                adminRepository.update(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin updated successfully"));
            } else {
                adminRepository.save(selectedAdmin);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin saved successfully"));
            }

            admins = adminRepository.getAll(); //getAll Refresh the admin list
            selectedAdmin = new Admin(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Admin already exists"));
        }
    }

    public void deleteAdmin(Admin admin) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            adminRepository.delete(admin.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin deleted successfully"));
            admins = adminRepository.getAll();//AdminsgetAllsh the admin list
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete admin"));
        }
    }

    public void prepareEditAdmin(Admin admin) {
        this.selectedAdmin = admin;
        this.editMode = true;
    }

    public void prepareNewAdmin() {
        this.selectedAdmin = new Admin();
        this.editMode = false;
    }

      public AdminRepository getAdminRepository() {
        return adminRepository;
    }

    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

//Getter and Setter for LazyTable    
      public LazyDataModel<Admin> getLazyAdmins() {
        return lazyAdmins;
    }

    public void setLazyAdmins(LazyDataModel<Admin> lazyAdmins) {
        this.lazyAdmins = lazyAdmins;
    }

}
