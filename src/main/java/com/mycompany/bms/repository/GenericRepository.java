/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

/**
 *
 * @author malina
 */

import javax.transaction.Transactional;
import java.util.List;

public abstract class GenericRepository<T, ID> {
    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    abstract public void save(T entity) ;

    @Transactional
    abstract public T getById(ID id); 

    @Transactional
    abstract public void update(T entity) ;

    @Transactional
    abstract public void delete(ID id) ;
        

    abstract public List<T> getAll();
        
}

