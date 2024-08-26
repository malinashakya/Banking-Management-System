package com.mycompany.bms.repository;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import org.primefaces.model.FilterMeta;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@Stateless
public class AccountRepository extends GenericRepository<Account, Long> {

    private static final String ACCOUNT_NUMBER_PREFIX = "AT";
    private static final SecureRandom RANDOM = new SecureRandom();

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    public AccountRepository() {
        super(Account.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    @Override
    public void save(Account account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            account.setAccountNumber(generateAccountNumber(account));
        }
        if (account.getBalance() == null) {
            account.setBalance(BigInteger.ZERO); // Default to zero
        }
        if (account.getInterestEarned() == null) {
            account.setInterestEarned(BigInteger.ZERO); // Default to zero
        }
        if (account.getPin() == null || account.getPin().isEmpty()) {
            account.setPin(generateRandomPin()); // Generate a random PIN
        }
        if (account.getStatus() == null) {
            account.setStatus(AccountStatusEnum.ACTIVE); // Default to ACTIVE
        }
        super.save(account);
    }

    /**
     * Generate a unique account number based on the desired format. Format:
     * AT-<AccountTypeAlias>-<CustomerID>-<AccountTypeID>-<SequenceNumber>
     *
     * @param account
     * @return
     */
    public String generateAccountNumber(Account account) {
        // Ensure accountTypeAlias is a String
        String accountTypeAlias = String.valueOf(account.getAccountType().getAccountType()).substring(0, 2); // Taking two letters from the AccountType as the alias
        String customerId = String.valueOf(account.getCustomer().getId());
        String accountTypeId = String.valueOf(account.getAccountType().getId());
        String sequenceNumber = getNextSequenceNumber(accountTypeAlias, customerId, accountTypeId);

        return String.format("%s-%s-%s-%s-%s", ACCOUNT_NUMBER_PREFIX, accountTypeAlias, customerId, accountTypeId, sequenceNumber);
    }

    /**
     * Get the next sequence number for the account number. You might need to
     * adjust this based on your sequence generation strategy.
     */
    private String getNextSequenceNumber(String accountTypeAlias, String customerId, String accountTypeId) {
        int lastSequenceNumber = getLastSequenceNumber(accountTypeAlias, customerId, accountTypeId);
        int nextSequenceNumber = lastSequenceNumber + 1;
        return String.format("%05d", nextSequenceNumber);
    }

    /**
     * Retrieve the last sequence number used for a specific account type,
     * customer, and account type ID. This method assumes a sequence number is
     * stored or calculated based on the highest number used.
     */
    private int getLastSequenceNumber(String accountTypeAlias, String customerId, String accountTypeId) {
        TypedQuery<String> query = getEntityManager()
                .createQuery("SELECT a.accountNumber FROM Account a WHERE a.accountNumber LIKE :prefix", String.class);
        query.setParameter("prefix", ACCOUNT_NUMBER_PREFIX + "-" + accountTypeAlias + "-" + customerId + "-" + accountTypeId + "-%");

        List<String> accountNumbers = query.getResultList();

        int maxSequenceNumber = 0;
        for (String accountNumber : accountNumbers) {
            String[] parts = accountNumber.split("-");
            if (parts.length > 4) {
                try {
                    int sequenceNumber = Integer.parseInt(parts[4]);
                    if (sequenceNumber > maxSequenceNumber) {
                        maxSequenceNumber = sequenceNumber;
                    }
                } catch (NumberFormatException e) {
                    // Handle potential parsing issues gracefully
                }
            }
        }

        return maxSequenceNumber;
    }

    /**
     * Generate a random 4-digit PIN.
     *
     * @return A 4-digit PIN.
     */
    public String generateRandomPin() {
        int pin = RANDOM.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
        return String.valueOf(pin);
    }

    public List<Account> getAccounts(int first, int pageSize, Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);

        // Apply filters
        Predicate[] predicates = createPredicates(cb, root, filters);
        if (predicates.length > 0) {
            cq.where(predicates);
        }

        // Create and execute the query
        TypedQuery<Account> query = getEntityManager().createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private Predicate[] createPredicates(CriteriaBuilder cb, Root<Account> root, Map<String, FilterMeta> filters) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();

            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                predicates.add(createPredicate(cb, root, key, filter.getFilterValue()));
            }
        }

        return predicates.toArray(new Predicate[0]);
    }

    public int countAccounts(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Account> root = cq.from(Account.class);

        // Select the count of Account entities
        cq.select(cb.count(root));

        // Create predicates based on the provided filters
        Predicate[] predicates = createPredicates(cb, root, filters);
        if (predicates.length > 0) {
            cq.where(predicates);
        }

        // Create and execute the query
        TypedQuery<Long> query = getEntityManager().createQuery(cq);
        return query.getSingleResult().intValue();
    }
}
