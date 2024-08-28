package com.mycompany.bms.model;

import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Account")
public class Account extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "account_type_id", nullable = false)
    public AccountType accountType;

    @Column(name = "balance", nullable = false)
    private BigInteger balance; // Default to zero

    @Column(name = "interest_earned", nullable = false)
    private BigInteger interestEarned; // Default to zero

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatusEnum status = AccountStatusEnum.ACTIVE; // Default to ACTIVE

    @Column(name = "pin", nullable = false)
    @Size(min = 4, max = 4, message = "PIN must be 4 digits")
    private String pin;

    // Constructors
    public Account() {
    }

    public Account(Customer customer, AccountType accountType, String accountNumber, String pin, AccountStatusEnum status) {
        this.customer = customer;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.status = status;
    }

    // Getters and Setters
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

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public BigInteger getInterestEarned() {
        return interestEarned;
    }

    public void setInterestEarned(BigInteger interestEarned) {
        this.interestEarned = interestEarned;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AccountStatusEnum status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return accountNumber != null ? accountNumber.equals(account.accountNumber) : account.accountNumber == null;
    }

    @Override
    public int hashCode() {
        return accountNumber != null ? accountNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Account{"
                + "accountNumber='" + accountNumber + '\''
                + ", balance=" + balance
                + '}';
    }

}
