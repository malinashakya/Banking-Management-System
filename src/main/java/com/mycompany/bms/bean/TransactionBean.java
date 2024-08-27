package com.mycompany.bms.bean;

import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.TransactionTypeEnum;
import com.mycompany.bms.repository.TransactionRepository;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.model.Account;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("transactionBean")
@ViewScoped
public class TransactionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    private GenericLazyDataModel<Transaction> lazyDataModel;
    private TransactionTypeEnum selectedTransaction;
    private boolean editMode = false;

    private Transaction selectedEntity;
    private String transferAccountNumber; // Field for the transfer account number

    @PostConstruct
    public void init() {
        if (selectedEntity == null) {
            selectedEntity = new Transaction();
        }
        lazyDataModel = new GenericLazyDataModel<>(transactionRepository, Transaction.class);
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
            Account account = selectedEntity.getAccount();
            BigInteger amount = selectedEntity.getAmount();

            if (selectedTransaction == TransactionTypeEnum.WITHDRAW || selectedTransaction == TransactionTypeEnum.TRANSFER) {
                if (account.getBalance().compareTo(amount) < 0) {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient balance for transaction"));
                    return;
                }
                account.setBalance(account.getBalance().subtract(amount));
            } else if (selectedTransaction == TransactionTypeEnum.DEPOSIT) {
                account.setBalance(account.getBalance().add(amount));
            }

            if (selectedTransaction == TransactionTypeEnum.TRANSFER) {
                Account targetAccount = accountRepository.findByAccountNumber(transferAccountNumber);
                if (targetAccount == null) {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Target account not found"));
                    return;
                }
                targetAccount.setBalance(targetAccount.getBalance().add(amount));
                accountRepository.update(targetAccount);
            }

            accountRepository.update(account);

            if (editMode) {
                transactionRepository.update(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transaction updated successfully"));
            } else {
                transactionRepository.save(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transaction saved successfully"));
            }
            selectedEntity = new Transaction();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update Transaction"));
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


    public Transaction getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Transaction selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public List<TransactionTypeEnum> getTransactionTypeList() {
    return Arrays.asList(TransactionTypeEnum.values());
}
    
    
}
