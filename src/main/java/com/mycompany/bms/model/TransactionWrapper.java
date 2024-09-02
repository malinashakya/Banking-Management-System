/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.model;

/**
 *
 * @author malina
 */
import java.util.Collections;
import java.util.List;

public class TransactionWrapper {

    private List<Transaction> transactions;

    //Transaction Wrapper to find the latest data of the transaction
    public TransactionWrapper(List<Transaction> transactions) {
        this.transactions = transactions;
        // Sort transactions in descending order by transactionTime
        Collections.sort(transactions, (t1, t2) -> t2.getTransactionTime().compareTo(t1.getTransactionTime()));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
