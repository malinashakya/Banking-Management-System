package com.mycompany.bms.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    @Size(min = 2, max = 20, message = "First name should be at least 2 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name should be a valid string")
    @NotEmpty(message = "First Name cannot be empty")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(min = 2, max = 20, message = "Last name should be at least 2 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name should be a valid string")
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;

    @Column(name = "address", nullable = false)
    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @Column(name = "contact", nullable = false, unique = true)
    @Size(min = 10, max = 10, message = "Contact number should be 10 digits")
    @NotEmpty(message = "Contact cannot be empty")
    private String contact;

    @Column(name = "username", nullable = false, unique = true)
    @Email(message = "Username must be a valid email address")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts; // Updated from accountTypes to accounts

    // Constructors
    public Customer() {
    }

    public Customer(String firstName, String lastName, String address, String contact, String username, String password, List<Account> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.password = password;
        if (accounts == null) {
            throw new IllegalArgumentException("Accounts must not be null");
        }
        this.accounts = accounts;
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
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

        return getId() != null && getId().equals(customer.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
    
      @Override
    public String toString() {
        return "Customer{" + "id=" + getId() + ", firstname=" + firstName +  ", lastname=" + lastName + ", address=" + address+
                ", contact=" + contact+ ", username=" + username+   '}';
    }
}
