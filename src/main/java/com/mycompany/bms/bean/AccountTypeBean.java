package com.mycompany.bms.bean;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.repository.AccountTypeRepository;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

@Named("accountTypeBean")
@ViewScoped
public class AccountTypeBean extends GenericBean<AccountType> {

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        if (selectedEntity == null) {
            selectedEntity = new AccountType();
        }
    }

    public AccountTypeEnum[] getAccountTypeEnums() {
        return AccountTypeEnum.values();
    }

    private AccountTypeEnum accountTypeEnumFilter;

    public AccountTypeEnum getAccountTypeEnumFilter() {
        return accountTypeEnumFilter;
    }

    public void setAccountTypeEnumFilter(AccountTypeEnum accountTypeEnumFilter) {
        this.accountTypeEnumFilter = accountTypeEnumFilter;
    }

    @Override
    protected List<AccountType> loadEntities(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        if (accountTypeEnumFilter != null) {
            filterBy.put("accountType", FilterMeta.builder() // Updated key
                    .field("accountType") // Match the attribute name in AccountType
                    .filterValue(accountTypeEnumFilter)
                    .build());
        }
        return accountTypeRepository.getAccountTypes(first, pageSize, filterBy);
    }

    @Override
    protected int countEntities(Map<String, FilterMeta> filterBy) {
        return accountTypeRepository.countAccountTypes(filterBy);
    }

    @Override
    protected void saveEntity(AccountType entity) {
        accountTypeRepository.save(entity);
    }

    @Override
    protected void updateEntity(AccountType entity) {
        accountTypeRepository.update(entity);
    }

    @Override
    protected void deleteEntity(Long id) {
        accountTypeRepository.delete(id);
    }

    @Override
    protected AccountType createNewEntity() {
        return new AccountType();
    }

    @Override
    protected Long getEntityId(AccountType entity) {
        return entity.getId();
    }
}
