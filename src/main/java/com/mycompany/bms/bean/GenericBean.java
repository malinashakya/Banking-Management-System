package com.mycompany.bms.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 * Abstract class providing generic CRUD operations and lazy loading for
 * entities.
 *
 * @param <T> Entity type
 */
public abstract class GenericBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    T selectedEntity; // Entity selected for editing
    private boolean editMode = false;

    // For Lazy Table
    LazyDataModel<T> lazyDataModel;
    private int pageSize = 5;

    // For Search Query
    Map<String, Object> searchCriteria = new HashMap<>();

    // Abstract methods to be implemented by subclasses
    protected abstract List<T> loadEntities(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy);

    protected abstract int countEntities(Map<String, FilterMeta> filterBy);

    protected abstract void saveEntity(T entity);

    protected abstract void updateEntity(T entity);

    protected abstract void deleteEntity(Long id);

    @PostConstruct
    public void init() {
        lazyDataModel = new LazyDataModel<T>() {

            @Override
            public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                if (filterBy == null) {
                    filterBy = new HashMap<>();
                }
                filterBy.putAll(searchCriteria.entrySet().stream()
                        .map(entry -> FilterMeta.builder().field(entry.getKey()).filterValue(entry.getValue()).build())
                        .collect(Collectors.toMap(FilterMeta::getField, Function.identity()))
                );
                List<T> entities = loadEntities(first, pageSize, sortBy, filterBy);
                this.setRowCount(countEntities(filterBy));
                return entities;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                if (filterBy == null) {
                    filterBy = new HashMap<>();
                }
                return countEntities(filterBy);
            }
        };
    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (editMode) {
                updateEntity(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Entity updated successfully"));
            } else {
                saveEntity(selectedEntity);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Entity saved successfully"));
            }
            selectedEntity = createNewEntity(); // Clear form after submission
            editMode = false; // Reset the edit mode flag
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update entity"));
        }
    }

    public void saveEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            updateEntity(selectedEntity);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Entity updated successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save/update entity"));
        }
    }

    public void deleteEntity(T entity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            deleteEntity(getEntityId(entity));
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Entity deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete entity"));
        }
    }

    public void prepareEditEntity(T entity) {
        this.selectedEntity = entity;
        this.editMode = true;
    }

    public void prepareNewEntity() {
        this.selectedEntity = createNewEntity();
        this.editMode = false;
    }

    // Abstract methods to be implemented by subclasses
    protected abstract T createNewEntity();

    protected abstract Long getEntityId(T entity);

    // Getters and setters
    public T getSelectedEntity() {
        if (selectedEntity == null) {
            selectedEntity = createNewEntity();
        }
        return selectedEntity;
    }

    public void setSelectedEntity(T selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public LazyDataModel<T> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<T> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        // Reset the row count when page size changes
        lazyDataModel.setRowCount(countEntities(new HashMap<>()));
    }
}
