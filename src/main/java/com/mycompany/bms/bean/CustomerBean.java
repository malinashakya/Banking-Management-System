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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

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
    private AccountType selectedAccountType;
    private List<Customer> customers;
    private List<AccountType> availableAccountTypes;
    private GenericLazyDataModel<Customer> lazyCustomers;
    private boolean editMode = false;
    private List<Account> customerAccounts;

    @PostConstruct
    public void init() {
        selectedCustomer = new Customer();
        lazyCustomers = new GenericLazyDataModel<>(customerRepository, Customer.class);
        availableAccountTypes = accountTypeRepository.getAll();
    }

    // Getters and setters
    public List<AccountType> getAvailableAccountTypes() {
        return availableAccountTypes;
    }

    public void setAvailableAccountTypes(List<AccountType> availableAccountTypes) {
        this.availableAccountTypes = availableAccountTypes;
    }

    public AccountType getSelectedAccountType() {
        return selectedAccountType;
    }

    public void setSelectedAccountType(AccountType selectedAccountType) {
        this.selectedAccountType = selectedAccountType;
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

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
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

    public List<Account> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(List<Account> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    // Business methods
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
            selectedCustomer = new Customer();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save customer"));
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

    public void prepareAddAccounts(Customer customer) {
        this.selectedCustomer = customer;
        this.selectedAccountType = null; // Reset selected account type
    }

    public void createAccount() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (selectedCustomer.getId() != null && selectedAccountType != null) {
                List<Account> existingAccounts = accountRepository.findByCustomerAndAccountType(selectedCustomer, selectedAccountType);

                if (!existingAccounts.isEmpty()) {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Customer already has an account of this type"));
                    return;
                }

                Account newAccount = new Account();
                newAccount.setCustomer(selectedCustomer);
                newAccount.setAccountType(selectedAccountType);
                newAccount.setBalance(BigInteger.ZERO);
                newAccount.setInterestEarned(BigInteger.ZERO);
                newAccount.setPin(accountRepository.generateRandomPin());
                newAccount.setStatus(AccountStatusEnum.ACTIVE);
                newAccount.setAccountNumber(accountRepository.generateAccountNumber(newAccount));

                accountRepository.save(newAccount);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account created successfully"));
            } else {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select an account type"));
            }
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create account"));
        }
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

    // Dashboard purpose
    public int getTotalCustomers() {
        return customerRepository.getAll().size();
    }

    public void prepareViewCustomer(Customer customer) {
        this.selectedCustomer = customer;
        this.customerAccounts = accountRepository.findByCustomerId(customer.getId()); // Fetch accounts of the customer
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("customerDetailDialogForm");
    }
}
