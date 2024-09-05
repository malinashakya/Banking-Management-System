package com.mycompany.bms.model;

import com.mycompany.bms.repository.AccountRepository;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import javax.inject.Inject;
import org.primefaces.model.SortOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountLazyDataModel extends LazyDataModel<Account> {

    @Inject
    private AccountRepository accountRepository;

    private Map<String, FilterMeta> filters;
    private Map<String, SortMeta> sortMeta;

    public List<Account> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        this.filters = convertToFilterMetaMap(filters); // Convert the filters map
        this.sortMeta = (Map<String, SortMeta>) sortMeta;

        // Perform sorting
        List<Account> accounts = accountRepository.getAccounts(first, pageSize, this.filters);
        setRowCount(accountRepository.countAccounts(this.filters));
        return accounts;
    }

    @Override
    public Account getRowData(String rowKey) {
        return accountRepository.findByAccountNumber(rowKey);
    }

    @Override
    public String getRowKey(Account account) {
        // Return a unique identifier for the account
        return account != null ? String.valueOf(account.getId()) : null;
    }

    public void setFilters(Map<String, FilterMeta> filters) {
        this.filters = filters;
    }

    public Map<String, FilterMeta> getFilters() {
        return filters;
    }

    @Override
    public int count(Map<String, FilterMeta> filters) {
        // Ensure this method returns the count of filtered accounts
        return accountRepository.countAccounts(filters);
    }

    @Override
    public List<Account> load(int first, int pageSize, Map<String, SortMeta> sortMeta, Map<String, FilterMeta> filters) {
        this.filters = filters;
        this.sortMeta = sortMeta;

        // Load accounts with applied filters
        List<Account> accounts = accountRepository.getAccounts(first, pageSize, this.filters);
        setRowCount(accountRepository.countAccounts(this.filters));
        return accounts;
    }

    // Helper method to convert Map<String, Object> to Map<String, FilterMeta>
    private Map<String, FilterMeta> convertToFilterMetaMap(Map<String, Object> filters) {
        Map<String, FilterMeta> filterMetaMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (entry.getValue() instanceof FilterMeta) {
                filterMetaMap.put(entry.getKey(), (FilterMeta) entry.getValue());
            }
        }
        return filterMetaMap;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
