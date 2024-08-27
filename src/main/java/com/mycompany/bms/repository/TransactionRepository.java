/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.Transaction;
import java.io.Serializable;
import javax.persistence.EntityManager;
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

    @PersistenceContext(name="BankingDS")
    private EntityManager entityManager;

    public TransactionRepository() {
        super(Transaction.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
     
}