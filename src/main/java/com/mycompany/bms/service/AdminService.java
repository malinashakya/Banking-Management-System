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