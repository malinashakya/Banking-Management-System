package com.mycompany.bms.model;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GenericLazyDataModel<T> extends LazyDataModel<T> {

    private final Function<Map<String, SortMeta>, List<T>> loadEntities;
    private final Function<Map<String, FilterMeta>, Integer> countEntities;

    public GenericLazyDataModel(Function<Map<String, SortMeta>, List<T>> loadEntities,
                                Function<Map<String, FilterMeta>, Integer> countEntities) {
        this.loadEntities = loadEntities;
        this.countEntities = countEntities;
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        List<T> data = loadEntities.apply(sortBy);
        setRowCount(countEntities.apply(filterBy));
        return data;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return countEntities.apply(filterBy);
    }
}
