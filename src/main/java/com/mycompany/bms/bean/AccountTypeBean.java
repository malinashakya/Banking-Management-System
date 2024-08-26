package com.mycompany.bms.bean;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.GenericLazyDataModel;
import com.mycompany.bms.repository.AccountTypeRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.HashMap;

@Named("accountTypeBean")
@ViewScoped
public class AccountTypeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private AccountTypeRepository accountTypeRepository;

    private AccountType selectedEntity;
    private boolean editMode = false;
    private GenericLazyDataModel<AccountType> lazyDataModel;
    private int pageSize = 5;
    private AccountTypeEnum accountTypeEnumFilter;

    @PostConstruct
    public void init() {
        if (selectedEntity == null) {
            selectedEntity = new AccountType();
        }
        lazyDataModel = new GenericLazyDataModel<>(accountTypeRepository, AccountType.class);

    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                accountTypeRepository.update(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type updated successfully"));
            } else {
                accountTypeRepository.save(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type saved successfully"));
            }
            selectedEntity = new AccountType();
            editMode = false;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update Account Type"));
        }
    }

    public void deleteEntity(AccountType entity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            accountTypeRepository.delete(entity.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Account Type deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete Account Type"));
        }
    }

    public void prepareEditEntity(AccountType entity) {
        this.selectedEntity = entity;
        this.editMode = true;
    }

    public void prepareNewEntity() {
        this.selectedEntity = new AccountType();
        this.editMode = false;
    }

    public AccountTypeEnum[] getAccountTypeEnums() {
        return AccountTypeEnum.values();
    }

    public AccountTypeEnum getAccountTypeEnumFilter() {
        return accountTypeEnumFilter;
    }

    public void setAccountTypeEnumFilter(AccountTypeEnum accountTypeEnumFilter) {
        this.accountTypeEnumFilter = accountTypeEnumFilter;
    }

    public AccountType getSelectedEntity() {
        if (selectedEntity == null) {
            selectedEntity = new AccountType();
        }
        return selectedEntity;
    }

    public void setSelectedEntity(AccountType selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public GenericLazyDataModel<AccountType> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(GenericLazyDataModel<AccountType> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        lazyDataModel.setRowCount(accountTypeRepository.countAccountTypes(new HashMap<>()));
    }
}
