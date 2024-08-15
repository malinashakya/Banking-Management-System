package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
//Indicates that the bean is a stateless session bean with no conversational state

public class AdminRepository extends GenericRepository{

  @PersistenceContext(unitName = "BankingDS")
private EntityManager entityManager;

//    EntityManager manages entity lifecycle, performs database operations, and executes queries in JPA.

  public AdminRepository() {
        super(Admin.class);
    }


    public Admin getByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult();
            //            This code executes a JPA query to find a single Admin entity where the username matches the provided parameter value.
//                :username is a placeholder in the query for a parameter value, allowing dynamic input.
//setParameter("username", username) binds the provided value to the :username placeholder in the query.
        } catch (Exception e) {
            return null;
        }
    }

    public Admin findByUsernameAndPassword(String username, String password) {
        TypedQuery<Admin> query = entityManager.createQuery(
                "SELECT a FROM Admin a WHERE a.username = :username AND a.password = :password", Admin.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultStream().findFirst().orElse(null);
    }
//    This code executes the query, retrieves the first result as a Stream, and returns it if present; otherwise, it returns null.
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
    
    public Admin getAdminByUsername(String username)
    {
        return getByUsername(username);
    }
    
    public Admin authenticate(String username, String password) {
        Admin admin = getByUsername(username);
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

    @Override
    public List<Admin> getAll() {
        return entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }

    @Override
    public void save(Object entity) {
             entityManager.persist(entity);
    }

    @Override
    public Object getById(Object id) {
          return entityManager.find(Admin.class, id);
    }

    @Override
    public void update(Object entity) {
        entityManager.merge(entity);    }

    @Override
    public void delete(Object id) {
        Admin entity = (Admin) getById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}
