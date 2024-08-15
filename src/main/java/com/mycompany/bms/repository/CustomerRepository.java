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
//Indicates that the bean is a stateless session bean with no conversational state

public class CustomerRepository extends GenericRepository<Customer, Long> {

    @PersistenceContext(name = "BankingDS")
//    Injects the EntityManager associated with the persistence unit named "BankingDS".

    private EntityManager entityManager;
//    EntityManager manages entity lifecycle, performs database operations, and executes queries in JPA.

    public CustomerRepository() {
        super(Customer.class);
    }

    @Transactional
//    Specifies that the method or class should execute within a transactional context, ensuring data consistency.
    @Override
    public void save(Customer customer) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(customer.getPassword(), salt);
        customer.setPassword(hashedPassword + ":" + salt);
        entityManager.persist(customer);

    }

    @Transactional
    @Override
    public Customer getById(Long id) {
        return entityManager.find(Customer.class, id);
//        entityManager.find(Customer.class, id);: Retrieves an Customer entity by its ID.
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
//        entityManager.merge(customer);: Updates an existing customer entity in the database.
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Customer customer = getById(id);
        if (customer != null) {
            entityManager.remove(customer);
//            entityManager.remove(customer);: Deletes the customer entity from the database.
        }
    }

    @Override
    public List<Customer> getAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
//        entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();: Executes a query to get a list of all User entities.
//User.class is used to specify the entity type for JPA operations, allowing the EntityManager to handle objects of that type for database operations and queries.
    }

    public Customer getByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT c FROM Customer c WHERE c.username = :username", Customer.class)
                    .setParameter("username", username)
                    .getSingleResult();
            //            This code executes a JPA query to find a single Customer entity where the username matches the provided parameter value.
//                :username is a placeholder in the query for a parameter value, allowing dynamic input.
//setParameter("username", username) binds the provided value to the :username placeholder in the query.
        } catch (Exception e) {
            return null;
        }
    }

    public Customer findByUsernameAndPassword(String username, String password) {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.username = :username AND c.password = :password", Customer.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultStream().findFirst().orElse(null);
    }
//    This code executes the query, retrieves the first result as a Stream, and returns it if present; otherwise, it returns null.

    public List<Customer> getByAccountType(Long accountTypeId) {
        try {
            return entityManager.createQuery("SELECT c FROM Customer c WHERE c.accountType.id = :accountTypeId", Customer.class)
                    .setParameter("accountTypeId", accountTypeId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    //For Password Hashing
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

    //Authentication of the Customer
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
    //For Lazy Table

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
