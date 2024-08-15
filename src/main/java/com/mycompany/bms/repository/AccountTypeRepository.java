package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountTypeRepository extends GenericRepository {

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    public AccountTypeRepository() {
        super(null);
    }

    public AccountTypeRepository(Class entityClass) {
        super(entityClass);
    }
  
    public List<AccountType> getAll() {
        return entityManager.createQuery("SELECT e FROM AccountType e", AccountType.class).getResultList();
    }

    @Override
    public void save(Object entity) {
             entityManager.persist(entity);
    }

    @Override
    public Object getById(Object id) {
          return entityManager.find(AccountType.class, id);
    }

    @Override
    public void update(Object entity) {
        entityManager.merge(entity);    }

    @Override
    public void delete(Object id) {
        AccountType entity = (AccountType) getById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
