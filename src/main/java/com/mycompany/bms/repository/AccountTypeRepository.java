package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

//    // Method to find AccountTypes by interestRate
//    public List<AccountType> findByInterestRate(float interestRate) {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
//        Root<AccountType> root = cq.from(AccountType.class);
//        cq.select(root);
//        Predicate interestRatePredicate = cb.equal(root.get(AccountType_.interestRate), interestRate);
//        cq.where(interestRatePredicate);
//        TypedQuery<AccountType> query = getEntityManager().createQuery(cq);
//        try {
//            return query.getResultList();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }
//
//    // Method to find AccountTypes by timePeriod
//    public List<AccountType> findByTimePeriod(int timePeriod) {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
//        Root<AccountType> root = cq.from(AccountType.class);
//        cq.select(root);
//        Predicate timePeriodPredicate = cb.equal(root.get(AccountType_.timePeriod), timePeriod);
//        cq.where(timePeriodPredicate);
//        TypedQuery<AccountType> query = getEntityManager().createQuery(cq);
//        try {
//            return query.getResultList();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }
//
//    // Method to find AccountTypes by accountTypeEnum
//    public List<AccountType> findByAccountTypeEnum(AccountTypeEnum accountTypeEnum) {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
//        Root<AccountType> root = cq.from(AccountType.class);
//        cq.select(root);
//        Predicate accountTypePredicate = cb.equal(root.get(AccountType_.accountType), accountTypeEnum);
//        cq.where(accountTypePredicate);
//        TypedQuery<AccountType> query = getEntityManager().createQuery(cq);
//        try {
//            return query.getResultList();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }
//
//    public List<AccountType> getAccountTypes(int first, int pageSize, Map<String, FilterMeta> filters) {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
//        Root<AccountType> root = cq.from(AccountType.class);
//        cq.select(root);
//
//        Predicate[] predicates = createPredicates(cb, root, filters);
//        if (predicates.length > 0) {
//            cq.where(predicates);
//        }
//
//        TypedQuery<AccountType> query = getEntityManager().createQuery(cq);
//        query.setFirstResult(first);
//        query.setMaxResults(pageSize);
//
//        return query.getResultList();
//    }
//
//    /**
//     * Count the number of AccountType entities matching the provided filters.
//     *
//     * @param filters A map of filters to apply to the query.
//     * @return The number of matching AccountType entities.
//     */
//    public int countAccountTypes(Map<String, FilterMeta> filters) {
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
//        Root<AccountType> root = cq.from(AccountType.class);
//
//        // Select the count of AccountType entities
//        cq.select(cb.count(root));
//
//        // Create predicates based on the provided filters
//        Predicate[] predicates = createPredicates(cb, root, filters);
//        if (predicates.length > 0) {
//            cq.where(predicates); // Apply predicates to the query
//        }
//
//        // Create and execute the query
//        TypedQuery<Long> query = getEntityManager().createQuery(cq);
//        return query.getSingleResult().intValue(); // Return the count of matching entities
//    }
//
//    /**
//     * Create an array of predicates based on the provided filters.
//     *
//     * @param cb The CriteriaBuilder instance.
//     * @param root The Root instance for the query.
//     * @param filters A map of filters to apply to the query.
//     * @return An array of predicates based on the filters.
//     */
//    private Predicate[] createPredicates(CriteriaBuilder cb, Root<AccountType> root, Map<String, FilterMeta> filters) {
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Iterate over the filters and create predicates
//        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
//            String key = entry.getKey();
//            FilterMeta filter = entry.getValue();
//
//            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
//                // Create predicate based on the filter key and value using metamodel types
//                predicates.add(createPredicate(cb, root, key, filter.getFilterValue()));
//            }
//        }
//
//        return predicates.toArray(new Predicate[0]); // Return the array of predicates
//    }
}
