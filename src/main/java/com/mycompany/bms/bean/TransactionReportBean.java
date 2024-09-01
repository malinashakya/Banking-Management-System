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
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named("transactionReportBean")
@ViewScoped
public class TransactionReportBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    private List<Transaction> allTransactions;
    private List<Transaction> savingsTransactions;
    private List<Transaction> fixedTransactions;

    private LocalDate startDate;
    private LocalDate endDate;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Customer loggedInCustomer = (Customer) externalContext.getSessionMap().get("loggedInCustomer");

        if (loggedInCustomer != null) {
            allTransactions = transactionRepository.getTransactionsByCustomerId(loggedInCustomer.getId());
            filterTransactionsByAccountType();
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
}
