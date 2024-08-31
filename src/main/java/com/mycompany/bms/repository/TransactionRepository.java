/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.Transaction;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author malina
 */
public class TransactionRepository extends GenericRepository<Transaction, Long> implements Serializable {

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    public TransactionRepository() {
        super(Transaction.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    //To fetch all the transaction of the particular Account Number
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);
        cq.where(cb.equal(root.get("account").get("accountNumber"), accountNumber));

        TypedQuery<Transaction> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

    //To get transactions done by particular id of customer
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);
        cq.where(cb.equal(root.get("account").get("customer").get("id"), customerId));

        TypedQuery<Transaction> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }
    // Fetch the opening balance at the start of the current month

    public BigInteger getOpeningBalanceForMonth(Long customerId) {
        // Define the start of the current month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);

        // Fetch the earliest balance after or on the start of the month
        return entityManager.createQuery(
                "SELECT t.amount FROM Transaction t WHERE t.account.customer.id = :customerId "
                + "AND t.date >= :startOfMonth ORDER BY t.date ASC", BigInteger.class)
                .setParameter("customerId", customerId)
                .setParameter("startOfMonth", startOfMonth)
                .setMaxResults(1)
                .getSingleResult();
    }

    // Fetch the closing balance as of the end of the current month
    public BigInteger getBalanceAsOfToday(Long customerId) {
        try {
            return entityManager.createQuery(
                    "SELECT a.balance FROM Account a WHERE a.customer.id = :customerId AND a.type = :accountType",
                    BigInteger.class)
                    .setParameter("customerId", customerId)
//                    .setParameter("accountType", AccountTypeEnum.SAVINGS) // Ensure to fetch only savings accounts
                    .getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where there is no account for the given customerId
            return BigInteger.ONE; // Return zero if no account is found
        }
    }

    // Fetch the latest transaction of the customer
    // Fetch the top 3 latest transactions of the customer
    public List<Transaction> getLatestTransactionByCustomer(Long customerId) {
        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId ORDER BY t.transactionTime DESC", Transaction.class)
                .setParameter("customerId", customerId)
                .setMaxResults(3) // Fetch the top 3 latest transactions
                .getResultList();
    }
}
