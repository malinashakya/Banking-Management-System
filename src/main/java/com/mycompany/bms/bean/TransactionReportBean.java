package com.mycompany.bms.bean;

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.repository.TransactionRepository;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("transactionReportBean")
@ViewScoped
public class TransactionReportBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private SessionCustomerBean sessionCustomerBean;

    private List<Transaction> allTransactions;
    private List<Transaction> savingsTransactions;
    private List<Transaction> fixedTransactions;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean loggedIn;
    private Customer loggedInCustomer;

    @PostConstruct
    public void init() {
        // Injected SessionCustomerBean
        loggedInCustomer = sessionCustomerBean.getCurrentCustomer();

        if (loggedInCustomer != null) {
            // Retrieve transactions for the logged-in customer
            allTransactions = transactionRepository.getTransactionsByCustomerId(loggedInCustomer.getId());
            filterTransactionsByAccountType();

            // Sort transactions initially
            sortTransactions();
        } else {

            // Initialize empty lists to avoid null pointers
            allTransactions = List.of();
            savingsTransactions = List.of();
            fixedTransactions = List.of();

        }
    }

    private void filterTransactionsByAccountType() {
        if (allTransactions != null) {
            savingsTransactions = allTransactions.stream()
                    .filter(t -> t.getAccount().getType() == AccountTypeEnum.SAVINGS)
                    .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Sort by date descending
                    .collect(Collectors.toList());

            fixedTransactions = allTransactions.stream()
                    .filter(t -> t.getAccount().getType() == AccountTypeEnum.FIXED)
                    .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate())) // Sort by date descending
                    .collect(Collectors.toList());
        }
    }

    public void filterTransactionsByDate() {
        if (startDate != null && endDate != null) {
            // Filter and sort savings transactions
            savingsTransactions = savingsTransactions.stream()
                    .filter(t -> !t.getTransactionTime().toLocalDate().isBefore(startDate)
                    && !t.getTransactionTime().toLocalDate().isAfter(endDate))
                    .sorted(Comparator.comparing(Transaction::getTransactionTime).reversed())
                    .collect(Collectors.toList());

            // Filter and sort fixed transactions
            fixedTransactions = fixedTransactions.stream()
                    .filter(t -> !t.getTransactionTime().toLocalDate().isBefore(startDate)
                    && !t.getTransactionTime().toLocalDate().isAfter(endDate))
                    .sorted(Comparator.comparing(Transaction::getTransactionTime).reversed())
                    .collect(Collectors.toList());
        }
    }

    private void sortTransactions() {
        if (savingsTransactions != null) {
            savingsTransactions = savingsTransactions.stream()
                    .sorted((t1, t2) -> t2.getTransactionTime().compareTo(t1.getTransactionTime())) // Sort by date descending
                    .collect(Collectors.toList());
        }

        if (fixedTransactions != null) {
            fixedTransactions = fixedTransactions.stream()
                    .sorted((t1, t2) -> t2.getTransactionTime().compareTo(t1.getTransactionTime())) // Sort by date descending
                    .collect(Collectors.toList());
        }
    }

    // Getters for savingsTransactions and fixedTransactions
    public List<Transaction> getSavingsTransactions() {
        return savingsTransactions;
    }

    public void setSavingsTransactions(List<Transaction> savingsTransactions) {
        this.savingsTransactions = savingsTransactions;
    }

    public List<Transaction> getFixedTransactions() {
        return fixedTransactions;
    }

    public void setFixedTransactions(List<Transaction> fixedTransactions) {
        this.fixedTransactions = fixedTransactions;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
