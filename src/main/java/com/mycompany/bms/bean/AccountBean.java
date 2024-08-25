package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.repository.AccountRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

@Named("accountBean")
@ViewScoped
public class AccountBean extends GenericBean<Account> {

    @Inject
    private AccountRepository accountRepository;

    private AccountStatusEnum selectedStatus;
    private Map<String, FilterMeta> filterBy = new HashMap<>(); // Initialize filterBy

    @Override
    protected List<Account> loadEntities(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        if (filterBy == null) {
            filterBy = new HashMap<>();
        }
        // Apply the selected status as a filter
        if (selectedStatus != null) {
            filterBy.put("status", FilterMeta.builder().field("status").filterValue(selectedStatus).build());
        }
        return accountRepository.getAccounts(first, pageSize, filterBy);
    }

    @Override
    protected int countEntities(Map<String, FilterMeta> filterBy) {
        if (filterBy == null) {
            filterBy = new HashMap<>();
        }
        // Apply the selected status as a filter
        if (selectedStatus != null) {
            filterBy.put("status", FilterMeta.builder().field("status").filterValue(selectedStatus).build());
        }
        return accountRepository.countAccounts(filterBy);
    }

    @Override
    protected void saveEntity(Account entity) {
        accountRepository.save(entity);
    }

    @Override
    protected void updateEntity(Account entity) {
        accountRepository.update(entity);
    }

    @Override
    protected void deleteEntity(Long id) {
        accountRepository.delete(id);
    }

    @Override
    protected Account createNewEntity() {
        return new Account();
    }

    @Override
    protected Long getEntityId(Account entity) {
        return entity.getId();
    }

    public void filterByStatus() {
        // Ensure filterBy is initialized
        if (filterBy == null) {
            filterBy = new HashMap<>();
        }

        // Refresh the data model
        lazyDataModel.setRowCount(countEntities(filterBy));
        lazyDataModel.load(0, getPageSize(), null, null);
    }

    public AccountStatusEnum getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(AccountStatusEnum selectedStatus) {
        this.selectedStatus = selectedStatus;
        filterByStatus();
    }

    public AccountStatusEnum[] getAccountStatusValues() {
        return AccountStatusEnum.values();
    }
}
