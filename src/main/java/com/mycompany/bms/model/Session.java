package com.mycompany.bms.model;

import java.util.Date;

/**
 * Represents a session in the banking application.
 * Includes fields for session details and methods to access and modify these details.
 */
public class Session {
    private int id;           
    private int customerId;   
    private Date loginTime;    
    private Date logoutTime;   

    // Default constructor
    public Session() {
    }

    // Parameterized constructor
    public Session(int id, int customerId, Date loginTime, Date logoutTime) {
        this.id = id;
        this.customerId = customerId;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for customerId
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // Getter and Setter for loginTime
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    // Getter and Setter for logoutTime
    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }
}
