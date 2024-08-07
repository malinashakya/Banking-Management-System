package com.mycompany.bms.service.impl;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import com.mycompany.bms.repository.impl.AdminRepositoryImpl;
import com.mycompany.bms.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository = new AdminRepositoryImpl();

    @Override
    public Admin findAdminById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminRepository.update(admin);
    }

    @Override
    public void deleteAdmin(Admin admin) {
        adminRepository.delete(admin);
    }
}
