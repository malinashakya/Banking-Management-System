package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;
import java.util.List;

public interface AdminRepository {
    Admin findById(Long id);
    List<Admin> findAll();
    void save(Admin admin);
    void update(Admin admin);
    void delete(Admin admin);
}
