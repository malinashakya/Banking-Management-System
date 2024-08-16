package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountTypeRepository extends GenericRepository<AccountType, Long>{

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;
    
    @PostConstruct
    public void init() {
        setEntityManager(entityManager); // Set the EntityManager after construction
    }

    public AccountTypeRepository() {
        super(AccountType.class);
    }

    public AccountTypeRepository(Class entityClass) {
        super(entityClass);
    }
  
}