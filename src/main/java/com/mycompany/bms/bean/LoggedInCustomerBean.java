package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.AccountTypeRepository;
import com.mycompany.bms.repository.CustomerRepository;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean loggedIn;

    @PostConstruct
    public void init() {
        PageAccessCustomerBean pageAccessBean = new PageAccessCustomerBean();
        System.out.println("LoggedInCustomerBean");

        // Check if the user is logged in
        loggedIn = pageAccessBean.isLoggedIn();
        if (loggedIn) {
            // Retrieve the logged-in customer from the session
            FacesContext facesContext = FacesContext.getCurrentInstance();
            loggedInCustomer = (Customer) facesContext.getExternalContext().getSessionMap().get("loggedInCustomer");

            // Initialize account types and customer accounts if logged in
            availableAccountTypes = accountTypeRepository.getAll();
            customerAccounts = accountRepository.findByCustomerId(loggedInCustomer.getId());
        } else {
            // Redirect to login page if not logged in
            pageAccessBean.checkLoginStatus();
            availableAccountTypes = new ArrayList<>();
            customerAccounts = new ArrayList<>();
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
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

    // Method to retrieve only savings accounts for the logged-in customer
    public List<Account> getCustomerSavingsAccounts() {
        return customerAccounts.stream()
                .filter(account -> account.getType() == AccountTypeEnum.SAVINGS)
                .collect(Collectors.toList());
    }
}
