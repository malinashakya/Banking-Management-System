package com.mycompany.bms.repository;

import com.mycompany.bms.model.Customer;
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
import javax.transaction.Transactional;
import org.primefaces.model.FilterMeta;

@Stateless
public class CustomerRepository extends GenericRepository<Customer, Long> {

    @PersistenceContext(name = "BankingDS")
    private EntityManager entityManager;

    public CustomerRepository() {
        super(Customer.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            String salt = generateSalt();
            String hashedPassword = hashPassword(customer.getPassword(), salt);
            customer.setPassword(hashedPassword + ":" + salt);
        }

        if (customer.getId() == null) {
            entityManager.persist(customer); // New entity, so persist
        } else {
            entityManager.merge(customer); // Existing entity, so merge
        }
    }

    @Transactional
    @Override
    public Customer getById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Transactional
    @Override
    public void update(Customer customer) {
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            String salt = generateSalt();
            String hashedPassword = hashPassword(customer.getPassword(), salt);
            customer.setPassword(hashedPassword + ":" + salt);
        }
        entityManager.merge(customer);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Customer customer = getById(id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }

    @Override
    public List<Customer> getAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    public Customer getByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Customer findByUsernameAndPassword(String username, String password) {
        try {
            TypedQuery<Customer> query = entityManager.createQuery(
                    "SELECT c FROM Customer c WHERE c.username = :username AND c.password = :password", Customer.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Customer> getByAccountType(Long accountTypeId) {
        try {
            return entityManager.createQuery("SELECT c FROM Customer c WHERE c.accountType.id = :accountTypeId", Customer.class)
                    .setParameter("accountTypeId", accountTypeId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

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

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public Customer authenticate(String username, String password) {
        Customer customer = getByUsername(username);
        if (customer != null) {
            String[] parts = customer.getPassword().split(":");
            String hashedPassword = parts[0];
            String salt = parts[1];
            if (hashedPassword.equals(hashPassword(password, salt))) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getCustomers(int first, int pageSize) {
        String query = "SELECT c FROM Customer c";
        return entityManager.createQuery(query, Customer.class)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public int countCustomers(Map<String, FilterMeta> filters) {
        String query = "SELECT COUNT(c) FROM Customer c";
        return ((Long) entityManager.createQuery(query).getSingleResult()).intValue();
    }
}
