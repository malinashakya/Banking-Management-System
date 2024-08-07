/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author malina
 */
/**
 * Represents a customer in the banking application.
 * Includes fields for customer details and methods to access and modify these details.
 */
@Entity
@Table(name="customers", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int pin;
    private String firstName;
    private String lastName;
    private String address;
    private String accountType;
    private String email;
    private String password;
    private String dateOfBirth;

    // Default constructor
    public Customer() {
    }

    // Parameterized constructor
    public Customer(int id, int pin, String firstName, String lastName, String address, 
                    String accountType, String email, String password, String dateOfBirth) {
        this.id = id;
        this.pin = pin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.accountType = accountType;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for pin
    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    // Getter and Setter for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and Setter for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for accountType
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for dateOfBirth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

