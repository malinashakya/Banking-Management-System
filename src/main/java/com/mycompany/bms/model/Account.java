package com.mycompany.bms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account")
public class Account implements Serializable {
//Serializable is like giving Java a way to package your objects for storage or transport and then unpack them later without losing any information.
 // Like in game, our level, health, weapon collected are remembered.   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @Column(name = "balance", nullable = false)
    private float balance;

    @Column(name = "interest_earned", nullable = false)
    private float interestEarned;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "pin", nullable = false)
    private String pin;

    // Constructors
    public Account() {}

    public Account(Customer customer, AccountType accountType, float balance, float interestEarned, String accountNumber, String status, String pin) {
        this.customer = customer;
        this.accountType = accountType;
        this.balance = balance;
        this.interestEarned = interestEarned;
        this.accountNumber = accountNumber;
        this.status = status;
        this.pin = pin;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getInterestEarned() {
        return interestEarned;
    }

    public void setInterestEarned(float interestEarned) {
        this.interestEarned = interestEarned;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
