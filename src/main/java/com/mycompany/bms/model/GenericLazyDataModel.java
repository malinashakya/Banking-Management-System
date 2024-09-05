package com.mycompany.bms.model;

import com.mycompany.bms.repository.GenericRepository;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

public class GenericLazyDataModel<T extends BaseEntity> extends LazyDataModel<T> {

    private final GenericRepository<T, Long> genericRepo;

    public GenericLazyDataModel(GenericRepository<T, Long> genericRepo, Class<T> entityClass) {
        this.genericRepo = genericRepo;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return genericRepo.countEntities(filterBy);
       
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
      
        List<T> loadedUsers=genericRepo.getEntities(first, pageSize, sortBy, filterBy);
             int numberOfUser = 0;
                for (Object user: loadedUsers){
                    System.out.println(user +" added");
                    numberOfUser++;
                }
                System.out.println("total loaded users = " +numberOfUser);
       
          return loadedUsers;
    }

}
