package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.AccountTypeRepository;
import com.mycompany.bms.repository.CustomerRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.faces.view.ViewScoped;

@Named("customerBean")
@ViewScoped
public class CustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private AccountTypeRepository accountTypeRepository;
    
    @Inject
    private AccountRepository accountRepository;

    private Customer selectedCustomer;
    private List<Customer> customers;
     private List<AccountType> accountTypes;
    private GenericLazyDataModel<Customer> lazyCustomers;
    private boolean editMode = false;   

    @PostConstruct
    public void init() {
        if (selectedCustomer == null) {
            selectedCustomer = new Customer();
        }

        // Initialize the GenericLazyDataModel with the customer repository
        lazyCustomers = new GenericLazyDataModel<>(customerRepository, Customer.class);
          accountTypes = accountTypeRepository.getAll();
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

    public GenericLazyDataModel<Customer> getLazyCustomers() {
        return lazyCustomers;
    }

    public void setLazyCustomers(GenericLazyDataModel<Customer> lazyCustomers) {
        this.lazyCustomers = lazyCustomers;
    }

    public void saveOrUpdateCustomer() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                customerRepository.update(selectedCustomer);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer updated successfully"));
            } else {
                customerRepository.save(selectedCustomer);
                createAccountForCustomer(selectedCustomer);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer saved successfully"));
            }
            selectedCustomer = new Customer();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save customer"));
        }
    }

    private void createAccountForCustomer(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setAccountType(customer.getAccountType());
        newAccount.setBalance(BigInteger.ZERO);
        newAccount.setInterestEarned(BigInteger.ZERO);
        newAccount.setPin(accountRepository.generateRandomPin());
        newAccount.setStatus(AccountStatusEnum.ACTIVE);
        newAccount.setAccountNumber(accountRepository.generateAccountNumber(newAccount));

        accountRepository.save(newAccount);
    }

    public void deleteCustomer(Customer customer) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            customerRepository.delete(customer.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer deleted successfully"));
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
}
