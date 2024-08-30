package com.mycompany.bms.bean;

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.repository.TransactionRepository;
import java.io.Serializable;
import java.util.Date;
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

    private Date startDate;
    private Date endDate;

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

    public void filterrTransactionsBydate() {
        if (startDate != null && endDate != null) {
            savingsTransactions = savingsTransactions.stream()
                    .filter(t -> !t.getDate().before(startDate) && !t.getDate().after(endDate) || t.getDate().equals(endDate))
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
