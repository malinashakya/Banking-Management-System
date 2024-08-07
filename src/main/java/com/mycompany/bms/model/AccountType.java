package com.mycompany.bms.model;

/**
 * Represents an account type with associated attributes.
 * Includes fields for account type details and methods to access and modify these details.
 */
public class AccountType {
    private int id;
    private AccountTypeEnum accountType;
    private float interestRate;
    private String maturityDate;

    // Enum for account types
    public enum AccountTypeEnum {
        SAVING, FIXED
    }

    // Default constructor
    public AccountType() {
    }

    // Parameterized constructor
    public AccountType(int id, AccountTypeEnum accountType, float interestRate, String maturityDate) {
        this.id = id;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for accountType
    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    // Getter and Setter for interestRate
    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    // Getter and Setter for maturityDate
    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }
}
