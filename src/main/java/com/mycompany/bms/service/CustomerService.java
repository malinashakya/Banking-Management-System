package com.mycompany.bms.service;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.CustomerRepository;
import com.mycompany.bms.repository.AccountTypeRepository; // Import the AccountType repository
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private AccountTypeRepository accountTypeRepository; // Inject the AccountType repository

    // Hashing password with SHA-256 and a salt
    public String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hashedPassword = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate a new salt
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public void saveCustomer(Customer customer) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(customer.getPassword(), salt);
        customer.setPassword(hashedPassword + ":" + salt);  // Store both hashed password and salt
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.getById(id);
    }

    public void updateCustomer(Customer customer) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(customer.getPassword(), salt);
        customer.setPassword(hashedPassword + ":" + salt);
        customerRepository.update(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.delete(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.getByUsername(username);
    }

    public Customer authenticateCustomer(String username, String password) {
        Customer customer = customerRepository.getByUsername(username);
        if (customer != null) {
            String[] parts = customer.getPassword().split(":");
            String storedHash = parts[0];
            String salt = parts[1];
            String hashedPassword = hashPassword(password, salt);
            if (storedHash.equals(hashedPassword)) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getCustomersByAccountType(Long accountTypeId) {
        return customerRepository.getByAccountType(accountTypeId);
    }

    public List<AccountType> getAllAccountTypes() {
        return accountTypeRepository.getAll(); // Fetch all AccountTypes from the repository
    }
}
