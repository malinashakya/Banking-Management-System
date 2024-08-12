package com.mycompany.bms.repository;

import com.mycompany.bms.model.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Stateless
//Indicates that the bean is a stateless session bean with no conversational state

public class CustomerRepository {
    @PersistenceContext(name = "BankingDS")
//    Injects the EntityManager associated with the persistence unit named "BankingDS".

    private EntityManager entityManager;
//    EntityManager manages entity lifecycle, performs database operations, and executes queries in JPA.

    @Transactional
//    Specifies that the method or class should execute within a transactional context, ensuring data consistency.
    public void save(Customer customer) {
        entityManager.persist(customer);
//        entityManager.persist(customer); saves a new Customer entity to the database.

    }

    @Transactional
    public Customer getById(Long id) {
        return entityManager.find(Customer.class, id);
//        entityManager.find(Customer.class, id);: Retrieves an Customer entity by its ID.
    }

    @Transactional
    public void update(Customer customer) {
        entityManager.merge(customer);
//        entityManager.merge(customer);: Updates an existing customer entity in the database.
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = getById(id);
        if (customer != null) {
            entityManager.remove(customer);
//            entityManager.remove(customer);: Deletes the customer entity from the database.
        }
    }

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
}
