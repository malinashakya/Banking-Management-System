package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.repository.AccountRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.HashMap;

@Named("accountBean")
@ViewScoped
public class AccountBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AccountRepository accountRepository;

    private Account selectedEntity;
    private boolean editMode = false;
    private GenericLazyDataModel<Account> lazyDataModel;
    private int pageSize = 5;
    private AccountStatusEnum accountStatusFilter;

    // The selected status from the dropdown
    private AccountStatusEnum selectedStatus;

    @PostConstruct
    public void init() {
        if (selectedEntity == null) {
            selectedEntity = new Account();
        }
        lazyDataModel = new GenericLazyDataModel<>(accountRepository, Account.class);

    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                accountRepository.update(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account updated successfully"));
            } else {
                accountRepository.save(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account saved successfully"));
            }
            selectedEntity = new Account();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update Account"));
        }
    }

    public void deleteEntity(Account entity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            accountRepository.delete(entity.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete Account"));
        }
    }

    public void saveEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            accountRepository.update(selectedEntity);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Entity updated successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update entity"));
        }
    }

    public void prepareEditEntity(Account entity) {
        this.selectedEntity = entity;
        this.editMode = true;
    }

    public void prepareNewEntity() {
        this.selectedEntity = new Account();
        this.editMode = false;
    }

    public AccountStatusEnum[] getAccountStatusValues() {
        return AccountStatusEnum.values();
    }

    public AccountStatusEnum getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(AccountStatusEnum selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public Account getSelectedEntity() {
        if (selectedEntity == null) {
            selectedEntity = new Account();
        }
        return selectedEntity;
    }

    public void setSelectedEntity(Account selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public GenericLazyDataModel<Account> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(GenericLazyDataModel<Account> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        lazyDataModel.setRowCount(accountRepository.countAccounts(new HashMap<>()));
    }
}
