/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

/**
 *
 * @author malina
 */
import java.util.List;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class GenericRepository<T, ID> {

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    abstract EntityManager getEntityManager();

    @Transactional
    public void save(T entity) {
        getEntityManager().persist(entity);
    }

    @Transactional
    public T getById(ID id) {
        return getEntityManager().find(entityClass, id);
    }

    @Transactional
    public void update(T entity) {
        getEntityManager().merge(entity);
    }

    @Transactional
    public void delete(ID id) {
        T entity = getById(id);
        if (entity != null) {
            getEntityManager().remove(entity);
        }
    }

    public List<T> getAll() {
        return getEntityManager().createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    @Transactional
    public void flush() {
        getEntityManager().flush();
    }
    
     // Utility method to initialize CriteriaBuilder, CriteriaQuery, and Root<T>
    protected CriteriaContext<T> createCriteriaContext() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        return new CriteriaContext<>(cb, cq, root);
    }

    // Inner class to hold CriteriaBuilder, CriteriaQuery, and Root
    protected static class CriteriaContext<T> {
        private final CriteriaBuilder cb;
        private final CriteriaQuery<T> cq;
        private final Root<T> root;

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
}
