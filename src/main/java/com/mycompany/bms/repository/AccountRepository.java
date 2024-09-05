package com.mycompany.bms.repository;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
import org.primefaces.model.FilterMeta;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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

    public String generateAccountNumber(Account account) {
        String accountTypeAlias = String.valueOf(account.getAccountType().getAccountType()).substring(0, 2); // Taking two letters from the AccountType as the alias
        String customerId = String.valueOf(account.getCustomer().getId());
        String accountTypeId = String.valueOf(account.getAccountType().getId());
        String sequenceNumber = getNextSequenceNumber(accountTypeAlias, customerId, accountTypeId);

        return String.format("%s-%s-%s-%s-%s", ACCOUNT_NUMBER_PREFIX, accountTypeAlias, customerId, accountTypeId, sequenceNumber);
    }

    private String getNextSequenceNumber(String accountTypeAlias, String customerId, String accountTypeId) {
        int lastSequenceNumber = getLastSequenceNumber(accountTypeAlias, customerId, accountTypeId);
        int nextSequenceNumber = lastSequenceNumber + 1;
        return String.format("%05d", nextSequenceNumber);
    }

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

    public String generateRandomPin() {
        int pin = RANDOM.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
        return String.valueOf(pin);
    }

    public List<Account> getAccounts(int first, int pageSize, Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        Join<Account, Customer> customerJoin = root.join("customer", javax.persistence.criteria.JoinType.LEFT);
        Join<Account, AccountType> accountTypeJoin = root.join("accountType", javax.persistence.criteria.JoinType.LEFT);

        // Apply filters
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();

            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                predicates.add(createPredicate(cb, root, customerJoin, accountTypeJoin, key, filter.getFilterValue()));
            }
        }

        // Add predicates to the query
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Execute query with pagination
        TypedQuery<Account> query = getEntityManager().createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    //Searching for all the columns, made transitive path 
    private Predicate createPredicate(CriteriaBuilder cb, Root<Account> root, Join<Account, Customer> customerJoin, Join<Account, AccountType> accountTypeJoin, String key, Object value) {
        switch (key) {
            case "accountNumber":
                // Filter by account number
                return cb.like(cb.lower(root.get("accountNumber")), "%" + value.toString().toLowerCase() + "%");
            case "customer.firstName":
                // Filter by first name
                return cb.like(cb.lower(customerJoin.get("firstName")), "%" + value.toString().toLowerCase() + "%");
            case "customer.lastName":
                // Filter by last name
                return cb.like(cb.lower(customerJoin.get("lastName")), "%" + value.toString().toLowerCase() + "%");

            case "status":
                // Filter by account status
                return cb.equal(root.get("status"), AccountStatusEnum.valueOf(value.toString().toUpperCase()));
            default:
                // Default predicate (no filtering)
                return cb.conjunction();
        }
    }

    public int countAccounts(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Account> root = cq.from(Account.class);
        Join<Account, Customer> customerJoin = root.join("customer", javax.persistence.criteria.JoinType.LEFT);
        Join<Account, AccountType> accountTypeJoin = root.join("accountType", javax.persistence.criteria.JoinType.LEFT);

        // Select the count of Account entities
        cq.select(cb.count(root));

        // Create predicates based on the provided filters
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();

            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                predicates.add(createPredicate(cb, root, customerJoin, accountTypeJoin, key, filter.getFilterValue()));
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Create and execute the query
        TypedQuery<Long> query = getEntityManager().createQuery(cq);
        return query.getSingleResult().intValue();
    }

    public Account findByAccountNumber(String accountNumber) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        cq.where(cb.equal(root.get("accountNumber"), accountNumber));
        TypedQuery<Account> query = getEntityManager().createQuery(cq);

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void updateAccountBalance(String accountNumber, BigInteger amount, boolean isDeposit) {
        Account account = findByAccountNumber(accountNumber);
        if (account != null) {
            BigInteger currentBalance = account.getBalance();
            if (isDeposit) {
                account.setBalance(currentBalance.add(amount));
            } else {
                if (currentBalance.compareTo(amount) >= 0) {
                    account.setBalance(currentBalance.subtract(amount));
                } else {
                    throw new IllegalArgumentException("Insufficient balance for withdrawal.");
                }
            }
            getEntityManager().merge(account);
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    @Transactional
    public void transferFunds(String fromAccountNumber, String toAccountNumber, BigInteger amount) {
        Account fromAccount = findByAccountNumber(fromAccountNumber);
        Account toAccount = findByAccountNumber(toAccountNumber);
        if (fromAccount != null && toAccount != null) {
            BigInteger fromAccountBalance = fromAccount.getBalance();
            if (fromAccountBalance.compareTo(amount) >= 0) {
                fromAccount.setBalance(fromAccountBalance.subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));
                getEntityManager().merge(fromAccount);
                getEntityManager().merge(toAccount);
            } else {
                throw new IllegalArgumentException("Insufficient balance for transfer.");
            }
        } else {
            throw new IllegalArgumentException("One or both accounts not found.");
        }
    }

    public List<Account> findAll() {
        TypedQuery<Account> query = getEntityManager().createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }

    public List<Account> findByCustomerAndAccountType(Customer customer, AccountType accountType) {
        return entityManager.createQuery("SELECT a FROM Account a WHERE a.customer = :customer AND a.accountType = :accountType", Account.class)
                .setParameter("customer", customer)
                .setParameter("accountType", accountType)
                .getResultList();
    }

    public List<Account> findByCustomerId(Long customerId) {
        String jpql = "SELECT a FROM Account a WHERE a.customer.id = :customerId";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
}
