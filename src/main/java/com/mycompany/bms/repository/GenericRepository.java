package com.mycompany.bms.repository;

import java.util.List;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Generic repository class providing basic CRUD operations for entities.
 *
 * @param <T>  The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public abstract class GenericRepository<T, ID> {

    // Class type of the entity
    private final Class<T> entityClass;

    /**
     * Constructor to initialize the repository with the entity class type.
     *
     * @param entityClass The class type of the entity.
     */
    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Method to get the EntityManager instance for database operations.
     *
     * @return The EntityManager instance.
     */
    abstract EntityManager getEntityManager();

    /**
     * Persist a new entity to the database.
     *
     * @param entity The entity to be persisted.
     */
    @Transactional
    public void save(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Find an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return The entity with the given identifier or null if not found.
     */
    @Transactional
    public T getById(ID id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Update an existing entity in the database.
     *
     * @param entity The entity to be updated.
     */
    @Transactional
    public void update(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Delete an entity by its identifier.
     *
     * @param id The identifier of the entity to be deleted.
     */
    @Transactional
    public void delete(ID id) {
        T entity = getById(id);
        if (entity != null) {
            getEntityManager().remove(entity);
        }
    }

    /**
     * Retrieve all entities of the type T from the database.
     *
     * @return A list of all entities.
     */
    public List<T> getAll() {
        return getEntityManager().createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    /**
     * Flush the current persistence context, synchronizing the database state.
     */
    @Transactional
    public void flush() {
        getEntityManager().flush();
    }

    /**
     * Utility method to create a CriteriaContext for building criteria queries.
     *
     * @return A CriteriaContext containing CriteriaBuilder, CriteriaQuery, and Root.
     */
    protected CriteriaContext<T> createCriteriaContext() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        return new CriteriaContext<>(cb, cq, root);
    }

    /**
     * Inner class to hold CriteriaBuilder, CriteriaQuery, and Root for a criteria query.
     *
     * @param <T> The type of the entity.
     */
    protected static class CriteriaContext<T> {

        private final CriteriaBuilder cb;
        private final CriteriaQuery<T> cq;
        private final Root<T> root;

        /**
         * Constructor to initialize the CriteriaContext with the given components.
         *
         * @param cb  The CriteriaBuilder instance.
         * @param cq  The CriteriaQuery instance.
         * @param root The Root instance.
         */
        public CriteriaContext(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root) {
            this.cb = cb;
            this.cq = cq;
            this.root = root;
        }

        public CriteriaBuilder getCb() {
            return cb;
        }

        public CriteriaQuery<T> getCq() {
            return cq;
        }

        public Root<T> getRoot() {
            return root;
        }
    }

    /**
     * Create a predicate for querying based on an attribute name and value.
     * Supports basic equality and string-based LIKE queries.
     *
     * @param cb           The CriteriaBuilder instance.
     * @param root         The Root instance for the query.
     * @param attributeName The name of the attribute to filter.
     * @param value        The value to compare with.
     * @param <Y>          The type of the attribute value.
     * @return A Predicate representing the condition.
     */
    protected <Y> Predicate createPredicate(CriteriaBuilder cb, Root<T> root, String attributeName, Y value) {
        Path<Y> path = root.get(attributeName);
        if (value instanceof String) {
            // Case-insensitive partial match for strings
            return cb.like(cb.lower(path.as(String.class)), "%" + ((String) value).toLowerCase() + "%");
        } else {
            // Exact match for other types
            return cb.equal(path, value);
        }
    }
}
