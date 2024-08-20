package com.mycompany.bms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "First Name cannot be empty")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;

    @Column(name = "address", nullable = false)
    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @Column(name = "contact", nullable = false, unique = true)
    @NotEmpty(message = "Contact cannot be empty")
    private String contact;

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    @NotEmpty(message = "Date of Birth cannot be empty")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "account_type_id", nullable = false)
    @NotEmpty(message = "Account Type cannot be empty")
    private AccountType accountType;

    // Constructors
    public Customer() {
    }

    public Customer(String firstName, String lastName, String address, String contact, String username, String password, LocalDate dateOfBirth, AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        if (accountType == null) {
            throw new IllegalArgumentException("AccountType must not be null");
        }
        this.accountType = accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
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

        Customer customer = (Customer) o;

        return getId() != null ? getId().equals(customer.getId()) : customer.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Customer{"
                + "id=" + getId()
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", username='" + username + '\''
                + '}';
    }
}
