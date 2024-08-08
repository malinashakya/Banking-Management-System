//package com.mycompany.bms.service;
//
//import com.mycompany.bms.model.Admin;
//import com.mycompany.bms.repository.AdminRepository;
//
//import javax.inject.Inject;
//import javax.transaction.Transactional;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.List;
//
//public class AdminService {
//
//    @Inject
//    private AdminRepository adminRepository;
//
//    // Hashing method
//    private String hashPassword(String password) {
//        if (password == null) {
//            return null;
//        }
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) {
//                sb.append(String.format("%02x", b));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error hashing password", e);
//        }
//    }
//
//    @Transactional
//    public void save(Admin admin) {
//        // Hash the password before saving
//        if (admin.getPassword() != null) {
//            admin.setPassword(hashPassword(admin.getPassword()));
//        }
//        adminRepository.save(admin);
//    }
//
//    public Admin getById(Long id) {
//        return adminRepository.getById(id);
//    }
//
//    public List<Admin> getAll() {
//        return adminRepository.getAll();
//    }
//
//    @Transactional
//    public void update(Admin admin) {
//        adminRepository.update(admin);
//    }
//
//    @Transactional
//    public void delete(Long id) {
//        adminRepository.delete(id);
//    }
//
//    // Verify password method
//    public boolean verifyPassword(Admin admin, String passwordToCheck) {
//        String hashedPasswordToCheck = hashPassword(passwordToCheck);
//        return hashedPasswordToCheck != null && hashedPasswordToCheck.equals(admin.getPassword());
//    }
//}
package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;

import javax.inject.Inject;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class AdminService {

    @Inject
    private AdminRepository adminRepository;

    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin getAdminById(Long id) {
        return adminRepository.getById(id);
    }

    public void updateAdmin(Admin admin) {
        adminRepository.update(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.delete(id);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.getAll();
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.getByUsername(username);
    }
}   