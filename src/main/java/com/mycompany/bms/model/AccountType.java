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

    @Column(name = "time_period", nullable = false) // Ensure this field is not nullable
    @NotNull(message = "Time Period is needed")
    private Integer timePeriod = 0; // Default value, or use a sensible default based on your needs

    // Constructors, getters, and setters
    public AccountType() {
    }

    public AccountType(AccountTypeEnum accountType, float interestRate, Integer timePeriod) {
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.timePeriod = timePeriod;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;

        // Automatically adjust the timePeriod based on the accountType
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
        // Apply condition only if the account type is FIXED
        if (this.accountType == AccountTypeEnum.FIXED && timePeriod != null) {
            this.timePeriod = timePeriod;
        } else if (this.accountType != AccountTypeEnum.FIXED) {
            this.timePeriod = 0; // Set to a default value or handle appropriately
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.accountType);
        hash = 67 * hash + Float.floatToIntBits(this.interestRate);
        hash = 67 * hash + Objects.hashCode(this.timePeriod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountType other = (AccountType) obj;
        if (Float.floatToIntBits(this.interestRate) != Float.floatToIntBits(other.interestRate)) {
            return false;
        }
        if (this.accountType != other.accountType) {
            return false;
        }
        return Objects.equals(this.timePeriod, other.timePeriod);
    }

    @Override
    public String toString() {
        return "" + getId();
    }

}
