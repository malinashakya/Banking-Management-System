package com.mycompany.bms.model;

import java.util.Date;

/**
 * Represents a transaction in the banking application.
 * Includes fields for transaction details and methods to access and modify these details.
 */
public class Transaction {
    private int id;
    private int customerID;
    private Date transactionTime;
    private TransactionType transactionType;
    private float amount;

    // Enum for transaction types
    public enum TransactionType {
        WITHDRAW, DEPOSIT, TRANSFER
    }

    // Default constructor
    public Transaction() {
    }

    // Parameterized constructor
    public Transaction(int id, int customerID, Date transactionTime, 
                       TransactionType transactionType, float amount) {
        this.id = id;
        this.customerID = customerID;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for customerID
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    // Getter and Setter for transactionTime
    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    // Getter and Setter for transactionType
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    // Getter and Setter for amount
    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
