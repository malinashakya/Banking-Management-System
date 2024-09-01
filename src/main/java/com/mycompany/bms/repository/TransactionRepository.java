/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.TransactionTypeEnum;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
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

    // Fetch the top 3 latest transactions of the customer
    public List<Transaction> getLatestTransactionByCustomer(Long customerId) {
        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId ORDER BY t.transactionTime DESC", Transaction.class)
                .setParameter("customerId", customerId)
                .setMaxResults(3) // Fetch the top 3 latest transactions
                .getResultList();
    }

    public BigInteger getOpeningBalanceForMonth(Long customerId) {
        // Define the start of the current month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);

        // Query to sum deposits and withdrawals separately
        String depositQuery = "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t "
                + "WHERE t.account.customer.id = :customerId AND t.date < :startOfMonth "
                + "AND t.transactionType = :depositType";

        String withdrawalQuery = "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t "
                + "WHERE t.account.customer.id = :customerId AND t.date < :startOfMonth "
                + "AND t.transactionType = :withdrawalType";

        BigInteger depositTotal = entityManager.createQuery(depositQuery, BigInteger.class)
                .setParameter("customerId", customerId)
                .setParameter("startOfMonth", startOfMonth)
                .setParameter("depositType", TransactionTypeEnum.DEPOSIT) // Assuming ENUM for deposit
                .getSingleResult();

        BigInteger withdrawalTotal = entityManager.createQuery(withdrawalQuery, BigInteger.class)
                .setParameter("customerId", customerId)
                .setParameter("startOfMonth", startOfMonth)
                .setParameter("withdrawalType", TransactionTypeEnum.WITHDRAW) // Assuming ENUM for withdrawal
                .getSingleResult();

        // Calculate the opening balance by subtracting withdrawals from deposits
        return depositTotal.subtract(withdrawalTotal);
    }

    public BigInteger getBalanceAsOfToday(Long customerId) {
        // Define the end of today
        LocalDate endOfToday = LocalDate.now();

        // Query to sum deposits and withdrawals separately up to today
        String depositQuery = "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t "
                + "WHERE t.account.customer.id = :customerId AND t.date <= :endOfToday "
                + "AND t.transactionType = :depositType";

        String withdrawalQuery = "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t "
                + "WHERE t.account.customer.id = :customerId AND t.date <= :endOfToday "
                + "AND t.transactionType = :withdrawalType";

        // Execute deposit query
        BigInteger depositTotal = entityManager.createQuery(depositQuery, BigInteger.class)
                .setParameter("customerId", customerId)
                .setParameter("endOfToday", endOfToday)
                .setParameter("depositType", TransactionTypeEnum.DEPOSIT) // Assuming ENUM for deposit
                .getSingleResult();

        // Execute withdrawal query
        BigInteger withdrawalTotal = entityManager.createQuery(withdrawalQuery, BigInteger.class)
                .setParameter("customerId", customerId)
                .setParameter("endOfToday", endOfToday)
                .setParameter("withdrawalType", TransactionTypeEnum.WITHDRAW) // Assuming ENUM for withdrawal
                .getSingleResult();

        // Calculate the balance by adding deposits and subtracting withdrawals
        return depositTotal.subtract(withdrawalTotal);
    }
}
