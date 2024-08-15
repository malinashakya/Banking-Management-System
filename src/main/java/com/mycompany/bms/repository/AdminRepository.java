package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.primefaces.model.FilterMeta;

@Stateless
public class AdminRepository extends GenericRepository<Admin, Long> {

    @PersistenceContext(unitName = "BankingDS")
    private EntityManager entityManager;

    public AdminRepository() {
        super(Admin.class);
    }

    public Admin getByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class)
                    .setParameter("username", username)
                    .getSingleResult();
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

//    public Admin getAdminByUsername(String username) {
//        return getByUsername(username);
//    }

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
    public void save(Admin entity) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(entity.getPassword(), salt);
        entity.setPassword(hashedPassword + ":" + salt);
        entityManager.persist(entity);
    }

    @Override
    public Admin getById(Long id) {
        return entityManager.find(Admin.class, id);
    }

    @Override
    public void update(Admin entity) {
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
            String salt = generateSalt();
            String hashedPassword = hashPassword(entity.getPassword(), salt);
            entity.setPassword(hashedPassword + ":" + salt);
        }
        entityManager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        Admin entity = getById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    //For Lazy Table
    public List<Admin> getAdmins(int first, int pageSize) {
        String query = "SELECT a FROM Admin a";
        return entityManager.createQuery(query, Admin.class)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public int countAdmins(Map<String, FilterMeta> filters) {
        String query = "SELECT COUNT(a) FROM Admin a";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }

}
