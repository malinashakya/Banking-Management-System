package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AdminService{
    @Inject
//    This injects the adminRepository
    private AdminRepository adminRepository;
    
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

    public void saveAdmin(Admin admin) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(admin.getPassword(), salt);
        admin.setPassword(hashedPassword + ":" + salt);  // Store both hashed password and salt
        adminRepository.save(admin);
    }
    
    public Admin getAdminById(Long id)
    {
        return adminRepository.getById(id);
    }
    
   public void updateAdmin(Admin admin) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(admin.getPassword(), salt);
        admin.setPassword(hashedPassword + ":" + salt);
        adminRepository.update(admin);
    }
    
    public void deleteAdmin(Long id)
    {
        adminRepository.delete(id);
    }
    
    public Admin getAdminByUsername(String username)
    {
        return adminRepository.getByUsername(username);
    }
    
    public Admin authenticate(String username, String password) {
        Admin admin = adminRepository.getByUsername(username);
        if (admin != null) {
            String[] parts = admin.getPassword().split(":");
            String hashedPassword = parts[0];
            String salt = parts[1];
            if (hashedPassword.equals(hashPassword(password, salt))) {
                return admin;
            }
        }
        return null;
    }
    
    public List<Admin> getAllAdmins()
    {
        return adminRepository.getAll();
    }
}