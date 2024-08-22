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
 *
 * @author malina
 * @param <T>
 */

public abstract class GenericBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    T selectedEntity; // Entity selected for editing

    private boolean editMode = false;

    // For Lazy Table
    private LazyDataModel<T> lazyDataModel;
    private int pageSize = 5;

    // For Search Query
    private Map<String, Object> searchCriteria = new HashMap<>();

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
//            This process makes sure that your search is done with all the specified criteria
//            and gives you the results and the total count of matching items.
            
//            Depth explanation
//                    Search Criteria: The rules you set for finding items (e.g., "author is J.K. Rowling").
//                    Entry Set: A list of these rules.
//                    Stream: A way to go through each rule one by one.
//                    Map: Convert each rule into a special format (FilterMeta).
//                    FilterMeta: Defines how to filter items based on your rules.
//                    Build: Finalize each filter rule into FilterMeta.
//                    Collect: Gather all these filters into one map.
//                    Put All: Add these filters to the existing filter list.
//                    Load Entities: Find and return items that match these filters.
//                    Count: Get the total number of items that match the filters.
            
            public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
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

//    public Map<String, Object> getSearchCriteria() {
//        return searchCriteria;
//    }
//
//    public void setSearchCriteria(Map<String, Object> searchCriteria) {
//        this.searchCriteria = searchCriteria;
//    }

}
