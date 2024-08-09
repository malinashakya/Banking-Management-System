package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;
import static com.sun.tools.javac.jvm.ByteCodes.ret;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Stateless
//Indicates that the bean is a stateless session bean with no conversational state

public class AdminRepository {

    @PersistenceContext(name = "BankingDS")
//    Injects the EntityManager associated with the persistence unit named "BankingDS".

    private EntityManager entityManager;
//    EntityManager manages entity lifecycle, performs database operations, and executes queries in JPA.

    @Transactional
//    Specifies that the method or class should execute within a transactional context, ensuring data consistency.
    public void save(Admin admin) {
        entityManager.persist(admin);
//        entityManager.persist(admin); saves a new Admin entity to the database.

    }

    @Transactional
    public Admin getById(Long id) {
        return entityManager.find(Admin.class, id);
//        entityManager.find(Admin.class, id);: Retrieves an Admin entity by its ID.
    }

    @Transactional
    public void update(Admin admin) {
        entityManager.merge(admin);
//        entityManager.merge(user);: Updates an existing user entity in the database.
    }

    @Transactional
    public void delete(Long id) {
        Admin admin = getById(id);
        if (admin != null) {
            entityManager.remove(admin);
//            entityManager.remove(user);: Deletes the user entity from the database.
        }
    }

    public List<Admin> getAll() {
        return entityManager.createQuery("SELECT a FROM admin a", Admin.class).getResultList();
//        entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();: Executes a query to get a list of all User entities.
//User.class is used to specify the entity type for JPA operations, allowing the EntityManager to handle objects of that type for database operations and queries.
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
}
