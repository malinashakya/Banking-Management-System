package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AdminService {

    private final AdminRepository adminRepository = new AdminRepository();

    // Hashing method
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public void save(Admin admin) {
        // Hash the password before saving
        admin.setPassword(hashPassword(admin.getPassword()));
        adminRepository.save(admin);
    }

    public Admin getById(Long id) {
        return adminRepository.getById(id);
    }

    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    public void update(Admin admin) {
        adminRepository.update(admin);
    }

    public void delete(Long id) {
        adminRepository.delete(id);
    }

    // Verify password method
    public boolean verifyPassword(Admin admin, String passwordToCheck) {
        String hashedPasswordToCheck = hashPassword(passwordToCheck);
        return hashedPasswordToCheck.equals(admin.getPassword());
    }
}
