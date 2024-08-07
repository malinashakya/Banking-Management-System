package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;

import java.util.List;

public class AdminService {

    private final AdminRepository adminRepository = new AdminRepository();

    public void save(Admin admin) {
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
}
