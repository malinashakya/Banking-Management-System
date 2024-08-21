package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import java.io.Serializable;
import java.util.HashMap;
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

    private boolean editMode = false;

    // For Lazy Table
    private LazyDataModel<Admin> lazyAdmins;
    private int pageSize = 5;

    // For Search Query
    private Long searchId; // Add this property
    private String searchUsername;
    private String searchName;

    @Inject
    private AdminRepository adminRepository;

    @PostConstruct
    public void init() {
        lazyAdmins = new LazyDataModel<Admin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Admin> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                if (searchId != null) {
                    filterBy.put("id", FilterMeta.builder()
                            .field("id")
                            .filterValue(searchId)
                            .build());
                }
                if (searchUsername != null && !searchUsername.isEmpty()) {
                    filterBy.put("username", FilterMeta.builder()
                            .field("username")
                            .filterValue(searchUsername)
                            .build());
                }
                if (searchName != null && !searchName.isEmpty()) {
                    filterBy.put("name", FilterMeta.builder()
                            .field("name")
                            .filterValue(searchName)
                            .build());
                }

                List<Admin> admins = adminRepository.getAdmins(first, pageSize, filterBy); // Fetch admins with filters
                this.setRowCount(adminRepository.countAdmins(filterBy)); // Set total number of records
                return admins;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return adminRepository.countAdmins(filterBy);
            }
        };
    }

    // Getters and setters
    public Admin getSelectedAdmin() {
        return selectedAdmin;
    }

    public void setSelectedAdmin(Admin selectedAdmin) {
        this.selectedAdmin = selectedAdmin;
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

    // Getter and Setter for LazyTable    
    public LazyDataModel<Admin> getLazyAdmins() {
        return lazyAdmins;
    }

    public void setLazyAdmins(LazyDataModel<Admin> lazyAdmins) {
        this.lazyAdmins = lazyAdmins;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        // Reset the row count when page size changes
        lazyAdmins.setRowCount(adminRepository.countAdmins(new HashMap<>()));
    }

    // Getter and Setter for the Search Query
    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public String getSearchUsername() {
        return searchUsername;
    }

    public void setSearchUsername(String searchUsername) {
        this.searchUsername = searchUsername;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    // Methods to filter by ID, username, and name
    public void filterById() {
        searchUsername = null; // Clear other search filters
        searchName = null;
        lazyAdmins.load(0, pageSize, null, new HashMap<>()); // Refresh the data
    }

    public void filterByUsername() {
        searchId = null; // Clear other search filters
        searchName = null;
        lazyAdmins.load(0, pageSize, null, new HashMap<>()); // Refresh the data
    }

    public void filterByName() {
        searchId = null; // Clear other search filters
        searchUsername = null;
        lazyAdmins.load(0, pageSize, null, new HashMap<>()); // Refresh the data
    }
}
