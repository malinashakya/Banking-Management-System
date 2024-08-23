package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.AccountTypeRepository;
import com.mycompany.bms.repository.CustomerRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@Named("customerBean")
@ViewScoped
public class CustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private AccountTypeRepository accountTypeRepository;

    private Customer selectedCustomer;
    private List<Customer> customers;
    private List<AccountType> accountTypes;
    private boolean editMode = false;
    private LazyDataModel<Customer> lazyCustomers;
    private int pageSize = 5;

    @PostConstruct
    public void init() {
        try {
            accountTypes = accountTypeRepository.getAll();
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
                createAccountForCustomer(selectedCustomer); // Create account after saving customer
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
    // Convert enum to string

    private void createAccountForCustomer(Customer customer) {

        Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setAccountType(customer.getAccountType()); // Ensure the account type matches the customer's account type
        newAccount.setBalance(0f);
        newAccount.setInterestEarned(0f);
        newAccount.setPin(accountRepository.generateRandomPin()); // Use the repository method to generate the pin
        newAccount.setStatus(AccountStatusEnum.ACTIVE); // Default status
        newAccount.setAccountNumber(accountRepository.generateAccountNumber(newAccount)); // Generate the account number

        accountRepository.save(newAccount);
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
