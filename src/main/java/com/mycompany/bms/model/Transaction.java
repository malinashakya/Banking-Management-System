package com.mycompany.bms.model;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author malina
 */
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_time", nullable = false)
    private Date transactionTime;

    @Column(name = "amount", nullable = false)
    private BigInteger amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    // Default constructor
    public Transaction() {
    Date now = new Date();
    this.transactionTime = now;
    this.date = now;
}


    // Constructor with parameters
    public Transaction(Account account, TransactionTypeEnum transactionType, Date transactionTime, BigInteger amount, Date date) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionTime = transactionTime;
        this.amount = amount;
        this.date = date;
    }

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }   

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Transaction that = (Transaction) o;

        if (!account.equals(that.account)) return false;
        if (transactionType != that.transactionType) return false;
        if (!transactionTime.equals(that.transactionTime)) return false;
        if (!amount.equals(that.amount)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + account.hashCode();
        result = 31 * result + transactionType.hashCode();
        result = 31 * result + transactionTime.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
