/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class AccountTypeRepository {

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    @Transactional
    public void save(AccountType accountType) {
        entityManager.persist(accountType);
    }

    @Transactional
    public AccountType getById(Long id) {
        return entityManager.find(AccountType.class, id);
    }

    @Transactional
    public void update(AccountType accountType) {
        entityManager.merge(accountType);
    }

    @Transactional
    public void delete(Long id) {
        AccountType accountType = getById(id);
        if (accountType != null) {
            entityManager.remove(accountType);
        }
    }

    public List<AccountType> getAll() {
        return entityManager.createQuery("SELECT a FROM AccountType a", AccountType.class).getResultList();
    }
}
