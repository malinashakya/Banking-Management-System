package com.mycompany.bms.repository;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.model.Admin_;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.FilterMeta;

/**
 * Repository class for managing {@link Admin} entities. Provides methods for
 * CRUD operations and authentication.
 */
@Stateless
public class AdminRepository extends GenericRepository<Admin, Long> {

    @PersistenceContext(unitName = "BankingDS")
    private EntityManager entityManager;

    /**
     * Constructor to initialize the repository with the Admin class.
     */
    public AdminRepository() {
        super(Admin.class);
    }

    /**
     * Retrieve the EntityManager for database operations.
     *
     * @return The EntityManager instance.
     */
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Find an Admin entity by its username.
     *
     * @param username The username of the Admin.
     * @return The Admin entity with the specified username, or null if not
     * found.
     */
    public Admin getByUsername(String username) {
        // Initialize Criteria API context with CriteriaBuilder, CriteriaQuery, and Root
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create a predicate for the username condition using metamodel type
        Predicate usernamePredicate = createPredicate(context.getCb(), context.getRoot(), Admin_.username.getName(), username);

        // Build the query to select the admin where username matches
        context.getCq().select(context.getRoot()).where(usernamePredicate);

        // Create and execute the query
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    /**
     * Find an Admin entity by its name.
     *
     * @param name The name of the Admin.
     * @return The Admin entity with the specified name, or null if not found.
     */
    public Admin getByName(String name) {
        // Initialize Criteria API context with CriteriaBuilder, CriteriaQuery, and Root
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create a predicate for the name condition using metamodel type
        Predicate namePredicate = createPredicate(context.getCb(), context.getRoot(), Admin_.name.getName(), name);

        // Build the query to select the admin where name matches
        context.getCq().select(context.getRoot()).where(namePredicate);

        // Create and execute the query
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    /**
     * Find an Admin entity by both username and password.
     *
     * @param username The username of the Admin.
     * @param password The password of the Admin.
     * @return The Admin entity with the specified username and password, or
     * null if not found.
     */
    public Admin findByUsernameAndPassword(String username, String password) {
        // Initialize Criteria API context with CriteriaBuilder, CriteriaQuery, and Root
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create predicates for both username and password using metamodel types
        Predicate usernamePredicate = createPredicate(context.getCb(), context.getRoot(), Admin_.username.getName(), username);
        Predicate passwordPredicate = createPredicate(context.getCb(), context.getRoot(), Admin_.password.getName(), password);

        // Combine predicates using 'AND'
        context.getCq().where(context.getCb().and(usernamePredicate, passwordPredicate));

        // Create and execute the query
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no result found
        }
    }

    /**
     * Hash a password with SHA-256 using a provided salt.
     *
     * @param password The plain text password to be hashed.
     * @param salt The salt to be used in hashing.
     * @return The Base64-encoded hashed password.
     */
    public String hashPassword(String password, String salt) {
        try {
            // Initialize SHA-256 digest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Update digest with salt
            digest.update(salt.getBytes());
            // Hash the password
            byte[] hashedPassword = digest.digest(password.getBytes());
            // Return hashed password encoded in Base64
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // RuntimeException if algorithm not found
        }
    }

    /**
     * Generate a new salt for password hashing.
     *
     * @return A Base64-encoded salt.
     */
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt); // Generate random bytes
        return Base64.getEncoder().encodeToString(salt); // Return salt encoded in Base64
    }

    /**
     * Authenticate an Admin by username and password.
     *
     * @param username The username of the Admin.
     * @param password The plain text password.
     * @return The Admin entity if authentication succeeds, or null if it fails.
     */
    public Admin authenticate(String username, String password) {
        // Retrieve Admin by username
        Admin admin = getByUsername(username);
        if (admin != null) {
            // Extract hashed password and salt from stored password
            String[] parts = admin.getPassword().split(":");
            String hashedPassword = parts[0];
            String salt = parts[1];
            // Check if the provided password matches the stored hashed password
            if (hashedPassword.equals(hashPassword(password, salt))) {
                return admin; // Return the authenticated Admin
            }
        }
        return null; // Return null if authentication fails
    }

    /**
     * Retrieve a list of Admin entities for pagination.
     *
     * @param first The index of the first result to retrieve.
     * @param pageSize The number of results to retrieve.
     * @param filters A map of filters to apply to the query.
     * @return A list of Admin entities matching the filters.
     */
    public List<Admin> getAdmins(int first, int pageSize, Map<String, FilterMeta> filters) {
        // Initialize Criteria API context with CriteriaBuilder, CriteriaQuery, and Root
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create predicates based on the provided filters
        Predicate[] predicates = createPredicates(context.getCb(), context.getRoot(), filters);
        if (predicates.length > 0) {
            context.getCq().where(predicates); // Apply predicates to the query
        }

        // Create and execute the query
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        query.setFirstResult(first); // Set the starting index
        query.setMaxResults(pageSize); // Set the maximum number of results

        return query.getResultList(); // Return the list of results
    }

    /**
     * Count the number of Admin entities matching the provided filters.
     *
     * @param filters A map of filters to apply to the query.
     * @return The number of matching Admin entities.
     */
    public int countAdmins(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Admin> root = cq.from(Admin.class);

        // Select the count of Admin entities
        cq.select(cb.count(root));

        // Create predicates based on the provided filters
        Predicate[] predicates = createPredicates(cb, root, filters);
        if (predicates.length > 0) {
            cq.where(predicates); // Apply predicates to the query
        }

        // Create and execute the query
        TypedQuery<Long> query = getEntityManager().createQuery(cq);
        return query.getSingleResult().intValue(); // Return the count of matching entities
    }

    /**
     * Create an array of predicates based on the provided filters.
     *
     * @param cb The CriteriaBuilder instance.
     * @param root The Root instance for the query.
     * @param filters A map of filters to apply to the query.
     * @return An array of predicates based on the filters.
     */
    private Predicate[] createPredicates(CriteriaBuilder cb, Root<Admin> root, Map<String, FilterMeta> filters) {
        List<Predicate> predicates = new ArrayList<>();

        // Iterate over the filters and create predicates
        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();

            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                // Create predicate based on the filter key and value using metamodel types
                predicates.add(createPredicate(cb, root, key, filter.getFilterValue()));
            }
        }

        return predicates.toArray(new Predicate[0]); // Return the array of predicates
    }
}
