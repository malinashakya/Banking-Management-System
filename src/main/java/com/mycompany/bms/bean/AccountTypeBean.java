package com.mycompany.bms.bean;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.service.AccountTypeService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("accountTypeBean")
@ViewScoped
public class AccountTypeBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private AccountType selectedAccountType; // AccountType selected for editing

    private List<AccountType> accountTypes;
    private boolean editMode = false;
    private List<AccountTypeEnum> accountTypeEnums; // For dropdown

    @Inject
    private AccountTypeService accountTypeService;

    @PostConstruct
    public void init() {
        accountTypes = accountTypeService.getAll(); // Load all account types when bean is initialized
        accountTypeEnums = List.of(AccountTypeEnum.values()); // Load enum values
    }

    // Getters and setters
    public AccountType getSelectedAccountType() {
        return selectedAccountType;
    }

    public void setSelectedAccountType(AccountType selectedAccountType) {
        this.selectedAccountType = selectedAccountType;
    }

    public List<AccountType> getAccountTypes() {
        return accountTypes;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public List<AccountTypeEnum> getAccountTypeEnums() {
        return accountTypeEnums;
    }

    public void setAccountTypeEnums(List<AccountTypeEnum> accountTypeEnums) {
        this.accountTypeEnums = accountTypeEnums;
    }

    public void saveOrUpdateAccountType() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                accountTypeService.update(selectedAccountType);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type updated successfully"));
            } else {
                accountTypeService.save(selectedAccountType);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type saved successfully"));
            }

            accountTypes = accountTypeService.getAll(); // Refresh the account type list
            selectedAccountType = new AccountType(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update account type"));
        }
    }

    public void deleteAccountType(AccountType accountType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            accountTypeService.delete(accountType.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type deleted successfully"));
            accountTypes = accountTypeService.getAll(); // Refresh the account type list
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete account type"));
        }
    }

    public void prepareEditAccountType(AccountType accountType) {
        this.selectedAccountType = accountType;
        this.editMode = true;
    }

    public void prepareNewAccountType() {
        this.selectedAccountType = new AccountType();
        this.editMode = false;
    }
}
