package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
@Stateless
public class AdminRepository {
   
    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;
    
    @Transactional
    public void save(Admin admin){
        entityManager.persist(admin);
    }
    
     public Admin getById(Long id){
        return entityManager.find(Admin.class, id);
    }
    
    @Transactional
    public void update(Admin admin){
        entityManager.merge(admin);
    }
    
    @Transactional
    public void delete(Long id){
        Admin admin = getById(id);
        if(admin != null){
            entityManager.remove(admin);
        }
    }
    
    public List<Admin> getAll(){
        return entityManager.createQuery("SELECT u FROM Admin u", Admin.class).getResultList();
    }
    
    
    public Admin getByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM Admin u WHERE u.username = :username", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    
    
    }
    
    
}
