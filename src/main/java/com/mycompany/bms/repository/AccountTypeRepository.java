package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.AccountType_;
import com.mycompany.bms.model.AccountType;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.primefaces.model.FilterMeta;

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
        CriteriaContext<AccountType> context = createCriteriaContext();
        Predicate interestRatePredicate = createPredicate(context.getCb(), context.getRoot(), AccountType_.interestRate.getName(), interestRate);
        context.getCq().select(context.getRoot()).where(interestRatePredicate);
        TypedQuery<AccountType> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to find AccountTypes by timePeriod
    public List<AccountType> findByTimePeriod(int timePeriod) {
        CriteriaContext<AccountType> context = createCriteriaContext();
        Predicate timePeriodPredicate = createPredicate(context.getCb(), context.getRoot(), AccountType_.timePeriod.getName(), timePeriod);
        context.getCq().select(context.getRoot()).where(timePeriodPredicate);
        TypedQuery<AccountType> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to find AccountTypes by accountTypeEnum
    public List<AccountType> findByAccountTypeEnum(AccountTypeEnum accountTypeEnum) {
        CriteriaContext<AccountType> context = createCriteriaContext();
        Predicate accountTypePredicate = createPredicate(context.getCb(), context.getRoot(), AccountType_.accountType.getName(), accountTypeEnum);
        context.getCq().select(context.getRoot()).where(accountTypePredicate);
        TypedQuery<AccountType> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<AccountType> getAccountTypes(int first, int pageSize, Map<String, FilterMeta> filters) {
        CriteriaContext<AccountType> context = createCriteriaContext();

        Predicate[] predicates = createPredicates(context.getCb(), context.getRoot(), filters);
        if (predicates.length > 0) {
            context.getCq().where(predicates);
        }

        TypedQuery<AccountType> query = getEntityManager().createQuery(context.getCq());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    /**
     * Count the number of AccountType entities matching the provided filters.
     *
     * @param filters A map of filters to apply to the query.
     * @return The number of matching AccountType entities.
     */
    public int countAccountTypes(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<AccountType> root = cq.from(AccountType.class);

        // Select the count of AccountType entities
        cq.select(cb.count(root));

        // Create predicates based on the provided filters
        Predicate[] predicates = createPredicates(cb, root, filters);
        if (predicates.length > 0) {
            cq.where(predicates); // Apply predicates to the query
        }

        // Create and execute the query
        TypedQuery<Long> query = getEntityManager().createQuery(cq);
        return query.getSingleResult().intValue(); // Return the count of matching entities
    }

    /**
     * Create an array of predicates based on the provided filters.
     *
     * @param cb The CriteriaBuilder instance.
     * @param root The Root instance for the query.
     * @param filters A map of filters to apply to the query.
     * @return An array of predicates based on the filters.
     */
    private Predicate[] createPredicates(CriteriaBuilder cb, Root<AccountType> root, Map<String, FilterMeta> filters) {
        List<Predicate> predicates = new ArrayList<>();

        // Iterate over the filters and create predicates
        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();

            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                // Create predicate based on the filter key and value using metamodel types
                predicates.add(createPredicate(cb, root, key, filter.getFilterValue()));
            }
        }

        return predicates.toArray(new Predicate[0]); // Return the array of predicates
    }
}
