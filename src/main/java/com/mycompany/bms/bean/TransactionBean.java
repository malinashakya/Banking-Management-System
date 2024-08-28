package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.TransactionTypeEnum;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.TransactionRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Named("transactionBean")
@ViewScoped
public class TransactionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    private GenericLazyDataModel<Transaction> lazyDataModel;
    private TransactionTypeEnum transactionType;
    private boolean editMode = false;

    private Transaction selectedEntity;
    private String targetAccountNumber; // Field for the transfer account number
    private BigInteger amount = BigInteger.ZERO;

    private List<Account> accountList;

    @PostConstruct
    public void init() {
        if (selectedEntity == null) {
            selectedEntity = new Transaction();
        }
        lazyDataModel = new GenericLazyDataModel<>(transactionRepository, Transaction.class);
        accountList = accountRepository.findAll();
        System.out.println("Account list initialized: " + accountList);
    }

    public GenericLazyDataModel<Transaction> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(GenericLazyDataModel<Transaction> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public void saveOrUpdateEntity() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    try {
        TransactionTypeEnum transactionTypes = selectedEntity.getTransactionType();
        Account account = selectedEntity.getAccount();
        amount = selectedEntity.getAmount();
        
        // Validation
        if (account == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Account is not selected."));
            return;
        }

        if (amount == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Amount cannot be null"));
            return;
        }

        if (transactionTypes == TransactionTypeEnum.TRANSFER && (targetAccountNumber == null || targetAccountNumber.isEmpty())) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Target account number cannot be null or empty for transfers"));
            return;
        }

        System.err.println("Amount:"+amount);
        System.err.println("Balance:"+account.getBalance());
        
        // Handle transaction types
        if (transactionTypes == TransactionTypeEnum.WITHDRAW || transactionTypes == TransactionTypeEnum.TRANSFER) {
            if (account.getBalance().compareTo(amount) < 0) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient balance for transaction"));
                return;
            }
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.update(account);
        } 
        else if (transactionTypes == TransactionTypeEnum.DEPOSIT) {
        account.setBalance(account.getBalance().add(amount));
             accountRepository.update(account);
        }

        // Handle transfers
        if (transactionTypes == TransactionTypeEnum.TRANSFER) {
            Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
            if (targetAccount == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Target account not found"));
                return;
            }
            
            // Create withdrawal transaction
            Transaction withdrawalTransaction = new Transaction();
            withdrawalTransaction.setAccount(account);
            withdrawalTransaction.setTransactionType(TransactionTypeEnum.WITHDRAW);
            withdrawalTransaction.setAmount(amount);
            withdrawalTransaction.setDate(selectedEntity.getDate());
            withdrawalTransaction.setTransactionTime(selectedEntity.getTransactionTime());
            transactionRepository.save(withdrawalTransaction);
            
            // Update source account
            accountRepository.update(account);

            // Create deposit transaction
            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(targetAccount);
            depositTransaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            depositTransaction.setAmount(amount);
            depositTransaction.setDate(selectedEntity.getDate());
            depositTransaction.setTransactionTime(selectedEntity.getTransactionTime());
            transactionRepository.save(depositTransaction);
            
            // Update target account
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
            accountRepository.update(targetAccount);

            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transfer completed successfully"));

        } else {
            // For non-transfer transactions
            if (editMode) {
                transactionRepository.update(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transaction updated successfully"));
            } else {
                transactionRepository.save(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transaction saved successfully"));
            }
        }

        // Reset state
        selectedEntity = new Transaction();
        editMode = false;
        
    } catch (Exception e) {
        e.printStackTrace(); // Print stack trace for debugging
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update Transaction: " + e.getMessage()));
    }
}

    public void deleteEntity(Transaction entity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            transactionRepository.delete(entity.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transaction deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete Transaction"));
        }
    }

    public void prepareNewEntity() {
        this.selectedEntity = new Transaction();
        this.selectedEntity.setAccount(new Account());
    }

    public void prepareEditEntity(Transaction entity) {
        this.selectedEntity = entity;
        if (this.selectedEntity.getAccount() == null) {
            this.selectedEntity.setAccount(new Account()); // Ensure Account is initialized
        }
        this.editMode = true;
    }

    // Getters and setters for the new fields
    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public Transaction getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Transaction selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<TransactionTypeEnum> getTransactionTypeList() {
        return Arrays.asList(TransactionTypeEnum.values());
    }

    // Getter for accountList
    public List<Account> getAccountList() {
        return accountList;
    }
    
     // Method to prepare for viewing a transaction
    public void prepareViewEntity(Transaction transaction) {
        // Set the selected transaction to the one passed as parameter
        this.selectedEntity = transaction;
    }

}