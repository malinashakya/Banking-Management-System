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

public abstract class GenericRepository<T, ID> {

//    @PersistenceContext(name = "BankingDS")
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Transactional
    public T getById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(ID id) {
        T entity = getById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    public List<T> getAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }
}
