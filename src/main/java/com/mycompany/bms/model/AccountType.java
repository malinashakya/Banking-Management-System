package com.mycompany.bms.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "account_type")
public class AccountType extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    @NotNull(message = "Account Type is needed")
    private AccountTypeEnum accountType;

    @Column(name = "interest_rate", nullable = false)
    @NotNull(message = "Interest Rate is needed")
    private float interestRate;

    @Column(name = "time_period", nullable = false)
    @NotNull(message = "Time Period is needed")
    private Integer timePeriod = 0; // Default value

    // Default constructor
    public AccountType() {
    }

    // Parameterized constructor
    public AccountType(AccountTypeEnum accountType, float interestRate, Integer timePeriod) {
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.timePeriod = (accountType == AccountTypeEnum.FIXED) ? timePeriod : 0;
    }

    // Getters and setters
    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        if (accountType != AccountTypeEnum.FIXED) {
            this.timePeriod = 0;
        }
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
        if (this.accountType == AccountTypeEnum.FIXED) {
            this.timePeriod = timePeriod != null ? timePeriod : 0;
        } else {
            this.timePeriod = 0;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, interestRate, timePeriod);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccountType other = (AccountType) obj;
        return Float.compare(other.interestRate, interestRate) == 0
                && Objects.equals(accountType, other.accountType)
                && Objects.equals(timePeriod, other.timePeriod);
    }

    @Override
    public String toString() {
        return "AccountType{" +
                "accountType=" + accountType +
                ", interestRate=" + interestRate +
                ", timePeriod=" + timePeriod +
                '}';
    }
}
