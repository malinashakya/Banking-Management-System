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

@Stateless
public class AdminRepository extends GenericRepository<Admin, Long> {

    @PersistenceContext(unitName = "BankingDS")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public AdminRepository() {
        super(Admin.class);
    }

    // Method to find Admin by username
    public Admin getByUsername(String username) {
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create a predicate for the username condition
        Predicate usernamePredicate = context.getCb().equal(context.getRoot().get(Admin_.username), username);

        // Set the query to select the admin where username matches
        context.getCq().select(context.getRoot()).where(usernamePredicate);

        // Execute the query and return the result
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No result found
        }
    }

    // Method to find Admin by name
    public Admin getByName(String name) {
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create a predicate for the name condition
        Predicate namePredicate = context.getCb().equal(context.getRoot().get(Admin_.name), name);

        // Set the query to select the admin where name matches
        context.getCq().select(context.getRoot()).where(namePredicate);

        // Execute the query and return the result
        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No result found
        }
    }

    public Admin findByUsernameAndPassword(String username, String password) {
        CriteriaContext<Admin> context = createCriteriaContext();

        // Create predicates for username and password
        Predicate usernamePredicate = context.getCb().equal(context.getRoot().get(Admin_.username), username);
        Predicate passwordPredicate = context.getCb().equal(context.getRoot().get(Admin_.password), password);

        // Combine predicates using 'AND'
        context.getCq().where(context.getCb().and(usernamePredicate, passwordPredicate));

        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No result found
        }
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
        return null; // Authentication failed
    }

    // For Lazy Table
    public List<Admin> getAdmins(int first, int pageSize, Map<String, FilterMeta> filters) {
        CriteriaContext<Admin> context = createCriteriaContext();

        Predicate[] predicates = createPredicates(context.getCb(), context.getRoot(), filters);
        if (predicates.length > 0) {
            context.getCq().where(predicates);
        }

        TypedQuery<Admin> query = getEntityManager().createQuery(context.getCq());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    // Keeping this method unchanged as it's optimized for counting
    public int countAdmins(Map<String, FilterMeta> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Admin> root = cq.from(Admin.class);

        cq.select(cb.count(root));

        Predicate[] predicates = createPredicates(cb, root, filters);
        if (predicates.length > 0) {
            cq.where(predicates);
        }

        TypedQuery<Long> query = getEntityManager().createQuery(cq);
        return query.getSingleResult().intValue();
    }

    private Predicate[] createPredicates(CriteriaBuilder cb, Root<Admin> root, Map<String, FilterMeta> filters) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
            String key = entry.getKey();
            FilterMeta filter = entry.getValue();
            if (filter.getFilterValue() != null && !filter.getFilterValue().toString().isEmpty()) {
                if (key.equals("username")) {
                    predicates.add(cb.like(cb.lower(root.get(Admin_.username)), "%" + filter.getFilterValue().toString().toLowerCase() + "%"));
                } else if (key.equals("name")) {
                    predicates.add(cb.like(cb.lower(root.get(Admin_.name)), "%" + filter.getFilterValue().toString().toLowerCase() + "%"));
                } else if (key.equals("id")) {
                    predicates.add(cb.equal(root.get(Admin_.id), Long.parseLong(filter.getFilterValue().toString())));
                }
            }
        }

        return predicates.toArray(new Predicate[0]);
    }
}
