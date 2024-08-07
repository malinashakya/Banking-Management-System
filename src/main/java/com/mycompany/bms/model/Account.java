package com.mycompany.bms.model;

/**
 * Represents an account with associated attributes.
 * Includes fields for account details and methods to access and modify these details.
 */
public class Account {
    private int id;
    private int customerID;
    private int accountTypeID;
    private int accountNumber;
    private float balance;
    private float interestEarned;
    private String status;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor
    public Account(int id, int customerID, int accountTypeID, int accountNumber, 
                   float balance, float interestEarned, String status) {
        this.id = id;
        this.customerID = customerID;
        this.accountTypeID = accountTypeID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.interestEarned = interestEarned;
        this.status = status;
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

    // Getter and Setter for accountTypeID
    public int getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(int accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    // Getter and Setter for accountNumber
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter and Setter for balance
    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    // Getter and Setter for interestEarned
    public float getInterestEarned() {
        return interestEarned;
    }

    public void setInterestEarned(float interestEarned) {
        this.interestEarned = interestEarned;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
