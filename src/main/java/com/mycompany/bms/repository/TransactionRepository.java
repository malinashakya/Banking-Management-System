/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.model.Transaction;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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

     public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);
        cq.where(cb.equal(root.get("account").get("customer").get("id"), customerId));

        TypedQuery<Transaction> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

}
