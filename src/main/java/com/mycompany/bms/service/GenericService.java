package com.mycompany.bms.service;

import com.mycompany.bms.repository.GenericRepository;
import java.util.List;

public abstract class GenericService<T, ID> {

    private final GenericRepository<T, ID> repository;

    protected GenericService(GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public T getById(ID id) {
        return repository.getById(id);
    }

    public void update(T entity) {
        repository.update(entity);
    }

    public void delete(ID id) {
        repository.delete(id);
    }

    public List<T> getAll() {
        return repository.getAll();
    }
}
