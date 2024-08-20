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

    private Class<T> entityClass;

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
}
