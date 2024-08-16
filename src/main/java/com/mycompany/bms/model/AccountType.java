package com.mycompany.bms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "account_type")

public class AccountType extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountTypeEnum accountType;

    @Column(name = "interest_rate", nullable = false)
    private float interestRate;

    @Column(name = "time_period", nullable = false) // Ensure this field is not nullable
    private Integer timePeriod = 0; // Default value, or use a sensible default based on your needs

    // Constructors, getters, and setters
    public AccountType() {
    }

    public AccountType(AccountTypeEnum accountType, float interestRate, Integer timePeriod) {
        this.accountType = accountType;
        this.interestRate = interestRate;
        setTimePeriod(timePeriod); // Use the setter to apply the condition
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

    public Integer getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(Integer timePeriod) {
        // Apply condition only if the account type is FIXED
        if (this.accountType == AccountTypeEnum.FIXED && timePeriod != null) {
            this.timePeriod = timePeriod;
        } else if (this.accountType != AccountTypeEnum.FIXED) {
            this.timePeriod = 0; // Set to a default value or handle appropriately
        }
    }
    
    @Override
    public String toString() {
        return "AccountType{" +
               "accountType='" + accountType + '\'' +
               ", interestRate=" + interestRate +
               ", timePeriod=" + timePeriod +
               '}';
    }
}
