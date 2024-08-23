/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.repository.AccountRepository;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

/**
 *
 * @author malina
 */
@Named("accountBean")
@ViewScoped
public class AccountBean extends GenericBean<Account> {

    @Inject
    private AccountRepository accountRepository;
    
   
    @Override
    protected List<Account> loadEntities(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
       return accountRepository.getAccounts(first, pageSize, filterBy);
    }

        @Override
    protected int countEntities(Map<String, FilterMeta> filterBy) {
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
   
    //For dropdown in the AccountStatus.xhtml
    public AccountStatusEnum[] getAccountStatusValues() {
        return AccountStatusEnum.values();
    }
}
