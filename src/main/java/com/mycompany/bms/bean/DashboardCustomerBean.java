package com.mycompany.bms.bean;

import com.mycompany.bms.model.Transaction;  // Ensure this import matches your project structure
import com.mycompany.bms.repository.TransactionRepository;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Bean for managing the customer dashboard, including displaying transactions.
 */
@ManagedBean(name = "dashboardBean")
@ViewScoped
public class DashboardCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Transaction> transactions;
    private boolean loggedIn;

    @Inject
    private SessionCustomerBean sessionCustomerBean;

    @Inject
    private TransactionRepository transactionRepository;

@PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Check if the user is logged in
        loggedIn = sessionCustomerBean.getCurrentCustomer() != null;

        if (loggedIn) {
            // Initialize transactions if logged in
            transactions = new ArrayList<>();
            loadTransactions();
        } else {
            // Initialize an empty list to avoid null pointer issues
            transactions = new ArrayList<>();
            
            // Optional: Inform the user if necessary
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Session Expired", "Please log in again to access this page."));
        }
    }

    // Load transactions from the repository for the logged-in customer
    private void loadTransactions() {
        Long loggedInCustomerId = sessionCustomerBean.getCurrentCustomer().getId();
        transactions = transactionRepository.getTransactionsByCustomerId(loggedInCustomerId);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
