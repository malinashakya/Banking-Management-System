package com.mycompany.bms.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_type")
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    //    @Enumerated(EnumType.STRING): Maps the accountType field to an ENUM type in the database
//    using the string representation of the enum values.
    @Column(name = "account_type", nullable = false)
    private AccountTypeEnum accountType;

    @Column(name = "interest_rate", nullable = false)
    private float interestRate;

    @Column(name = "maturity_date")
    private Date maturityDate;

    // Constructors, getters, and setters
    public AccountType() {
    }

    public AccountType(AccountTypeEnum accountType, float interestRate, Date maturityDate) {
        this.accountType = accountType;
        this.interestRate = interestRate;
        setMaturityDate(maturityDate); // Use the setter to apply the condition
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        if (this.accountType == AccountTypeEnum.FIXED) {
            this.maturityDate = maturityDate;
        } else {
            this.maturityDate = null; // Clear maturityDate if it's not a fixed account
        }
    }
}
