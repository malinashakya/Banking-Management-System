package com.mycompany.bms.repository;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.PersistenceContext;

@Named
@Stateless

public class AccountRepository extends GenericRepository<Account, Long> {

    private static final String ACCOUNT_NUMBER_PREFIX = "AT";
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountRepository() {
        super(Account.class);
    }

       @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;


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
            account.setBalance(0.0f); // Default to zero
        }
        if (account.getInterestEarned() == null) {
            account.setInterestEarned(0.0f); // Default to zero
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
     * @param account
     * @return 
     */
    public String generateAccountNumber(Account account) {
        // Ensure accountTypeAlias is a String
        String accountTypeAlias = String.valueOf(account.getAccountType().getAccountType()).substring(0, 2); // Taking three letters from the AccountType as the alias
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
        // Example sequence generator, replace with your implementation
        // In a real-world scenario, use a database sequence or similar approach
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
}
