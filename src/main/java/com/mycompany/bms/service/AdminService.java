package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import java.util.List;

public interface AdminService {
    Admin findAdminById(Long id);
    List<Admin> findAllAdmins();
    void saveAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteAdmin(Admin admin);
}
