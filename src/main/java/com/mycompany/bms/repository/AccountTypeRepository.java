package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.AccountTypeEnum;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class AccountTypeRepository extends GenericRepository<AccountType, Long> {

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    public AccountTypeRepository() {
        super(AccountType.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    // Method to find AccountTypes by interestRate
    public List<AccountType> findByInterestRate(float interestRate) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaBuilder: Used to construct different parts of a query in a type-safe way
        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
//     CriteriaQuery: Represents the query itself
        Root<AccountType> accountType = cq.from(AccountType.class);
//       Root: Defines the entity that the query will be selecting from, i.e. which table

        // Create a predicate for the condition
        Predicate interestRatePredicate = cb.equal(accountType.get("interestRate"), interestRate);
//        The Predicate interestRatePredicate is a condition that will be applied to the query. It represents the SQL condition WHERE interestRate = :interestRate.
        
        // Set the query to select account types where interestRate matches
        cq.select(accountType).where(interestRatePredicate);

        // Execute the query and return the result
        return getEntityManager().createQuery(cq).getResultList();
    }

    // Method to find AccountTypes by accountTypeEnum
    public List<AccountType> findByAccountTypeEnum(AccountTypeEnum accountTypeEnum) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
        Root<AccountType> accountType = cq.from(AccountType.class);

        // Create a predicate for the condition
        Predicate accountTypePredicate = cb.equal(accountType.get("accountType"), accountTypeEnum);

        // Set the query to select account types where accountType matches
        cq.select(accountType).where(accountTypePredicate);

        // Execute the query and return the result
        return getEntityManager().createQuery(cq).getResultList();
    }
}
