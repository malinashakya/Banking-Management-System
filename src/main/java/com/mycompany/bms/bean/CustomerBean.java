package com.mycompany.bms.bean;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.AccountTypeRepository;
import com.mycompany.bms.repository.CustomerRepository;
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

@Named("customerBean")
@ViewScoped
public class CustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Customer selectedCustomer; // Customer selected for editing

    private List<Customer> customers;
    private List<AccountType> accountTypes;
    private boolean editMode = false;

    // For Lazy Table
    private LazyDataModel<Customer> lazyCustomers;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @PostConstruct
    public void init() {
        try {
            accountTypes = accountTypeRepository.getAll();

            // For lazy table
            lazyCustomers = new LazyDataModel<Customer>() {
                private static final long serialVersionUID = 1L;

                @Override
                public int count(Map<String, FilterMeta> filterBy) {
                    return customerRepository.countCustomers(filterBy);
                }

                @Override
                public List<Customer> load(int first, int pageSize,
                                           Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    List<Customer> customers = customerRepository.getCustomers(first, pageSize);
                    this.setRowCount(customerRepository.countCustomers(filterBy));
                    return customers;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AccountType> getAccountTypes() {
        return accountTypes;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void saveOrUpdateCustomer() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                customerRepository.update(selectedCustomer);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer updated successfully"));
            } else {
                customerRepository.save(selectedCustomer);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer saved successfully"));
            }

            // Refresh lazy table
            lazyCustomers = new LazyDataModel<Customer>() {
                private static final long serialVersionUID = 1L;

                @Override
                public int count(Map<String, FilterMeta> filterBy) {
                    return customerRepository.countCustomers(filterBy);
                }

                @Override
                public List<Customer> load(int first, int pageSize,
                                           Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    List<Customer> customers = customerRepository.getCustomers(first, pageSize);
                    this.setRowCount(customerRepository.countCustomers(filterBy));
                    return customers;
                }
            };
            
            selectedCustomer = new Customer(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save customer"));
        }
    }

    public void deleteCustomer(Customer customer) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            customerRepository.delete(customer.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer deleted successfully"));
            
            // Refresh lazy table
            lazyCustomers = new LazyDataModel<Customer>() {
                private static final long serialVersionUID = 1L;

                @Override
                public int count(Map<String, FilterMeta> filterBy) {
                    return customerRepository.countCustomers(filterBy);
                }

                @Override
                public List<Customer> load(int first, int pageSize,
                                           Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                    List<Customer> customers = customerRepository.getCustomers(first, pageSize);
                    this.setRowCount(customerRepository.countCustomers(filterBy));
                    return customers;
                }
            };
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete customer"));
        }
    }

    public void prepareEditCustomer(Customer customer) {
        this.selectedCustomer = customer;
        this.editMode = true;
    }

    public void prepareNewCustomer() {
        this.selectedCustomer = new Customer();
        this.editMode = false;
    }

    public LazyDataModel<Customer> getLazyCustomers() {
        return lazyCustomers;
    }

    public void setLazyCustomers(LazyDataModel<Customer> lazyCustomers) {
        this.lazyCustomers = lazyCustomers;
    }
}
