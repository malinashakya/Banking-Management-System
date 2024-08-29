package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.TransactionTypeEnum;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.TransactionRepository;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("transactionCustomerBean")
@ViewScoped
public class TransactionCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    private Transaction selectedEntity;
    private String targetAccountNumber;
    private BigInteger amount = BigInteger.ZERO;
    private List<Account> accountList; // List for source and target accounts

    @PostConstruct
    public void init() {
        selectedEntity = new Transaction();
        // Initialize accountList with only savings accounts
        accountList = accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatusEnum.ACTIVE
                        && account.getType() == AccountTypeEnum.SAVINGS)
                .collect(Collectors.toList());
    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
         
            Account sourceAccount = selectedEntity.getAccount();
            Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);

            if (targetAccount == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Target account not found"));
                return;
            }

            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient balance"));
                return;
            }

            // Perform the transfer
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            targetAccount.setBalance(targetAccount.getBalance().add(amount));

            // Save transactions
            Transaction withdrawalTransaction = new Transaction();
            withdrawalTransaction.setAccount(sourceAccount);
            withdrawalTransaction.setTransactionType(TransactionTypeEnum.WITHDRAW);
            withdrawalTransaction.setAmount(amount);
            transactionRepository.save(withdrawalTransaction);

            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(targetAccount);
            depositTransaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            depositTransaction.setAmount(amount);
            transactionRepository.save(depositTransaction);

            // Update accounts
            accountRepository.update(sourceAccount);
            accountRepository.update(targetAccount);

            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transfer completed successfully"));

            selectedEntity = new Transaction(); // Reset the form

        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to complete transfer: " + e.getMessage()));
        }
    }

    // Getters and setters
    public Transaction getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Transaction selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
