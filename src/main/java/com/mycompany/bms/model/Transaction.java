package com.mycompany.bms.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "Amount cannot be empty")
    private BigInteger amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    // Default constructor
    public Transaction() {
        LocalDateTime now = LocalDateTime.now();
        this.transactionTime = now;
        this.date = now.toLocalDate();
    }

    // Constructor with parameters
    public Transaction(Account account, TransactionTypeEnum transactionType, LocalDateTime transactionTime, BigInteger amount, LocalDate date) {
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

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Transaction that = (Transaction) o;

        if (!account.equals(that.account)) {
            return false;
        }
        if (transactionType != that.transactionType) {
            return false;
        }
        if (!transactionTime.equals(that.transactionTime)) {
            return false;
        }
        if (!amount.equals(that.amount)) {
            return false;
        }
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
