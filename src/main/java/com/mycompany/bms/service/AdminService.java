package com.mycompany.bms.service;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AdminService{
    @Inject
//    This injects the adminRepository
    private AdminRepository adminRepository;
    
    public void saveAdmin(Admin admin)
    {
        adminRepository.save(admin);
    }
    
    public Admin getAdminById(Long id)
    {
        return adminRepository.getById(id);
    }
    
    public void updateAdmin(Admin admin)
    {
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
    
    public Admin authenticate(String username, String password)
    {
        return adminRepository.findByUsernameAndPassword(username, password);
    }
    
    public List<Admin> getAllAdmins()
    {
        return adminRepository.getAll();
    }
}