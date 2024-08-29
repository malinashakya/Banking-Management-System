package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
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

@Named("loggedInCustomerBean")
@ViewScoped
public class LoggedInCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @Inject
    private AccountRepository accountRepository;

    private Customer loggedInCustomer;
    private AccountType selectedAccountType;
    private List<AccountType> availableAccountTypes;
    private List<Account> customerAccounts;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        loggedInCustomer = (Customer) facesContext.getExternalContext().getSessionMap().get("loggedInCustomer");
        if (loggedInCustomer != null) {
            availableAccountTypes = accountTypeRepository.getAll();
            customerAccounts = accountRepository.findByCustomerId(loggedInCustomer.getId());
        }
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public AccountType getSelectedAccountType() {
        return selectedAccountType;
    }

    public void setSelectedAccountType(AccountType selectedAccountType) {
        this.selectedAccountType = selectedAccountType;
    }

    public List<AccountType> getAvailableAccountTypes() {
        return availableAccountTypes;
    }

    public void setAvailableAccountTypes(List<AccountType> availableAccountTypes) {
        this.availableAccountTypes = availableAccountTypes;
    }

    public List<Account> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(List<Account> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }
}
