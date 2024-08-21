package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

@Named("adminBean")
public class AdminBean extends GenericBean<Admin> {

    @Inject
    private AdminRepository adminRepository;

    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    protected List<Admin> loadEntities(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return adminRepository.getAdmins(first, pageSize, filterBy);
    }

    @Override
    protected int countEntities(Map<String, FilterMeta> filterBy) {
        return adminRepository.countAdmins(filterBy);
    }

    @Override
    protected void saveEntity(Admin entity) {
        adminRepository.save(entity);
    }

    @Override
    protected void updateEntity(Admin entity) {
        adminRepository.update(entity);
    }

    @Override
    protected void deleteEntity(Long id) {
        adminRepository.delete(id);
    }

    @Override
    protected Admin createNewEntity() {
        return new Admin();
    }

    @Override
    protected Long getEntityId(Admin entity) {
        return entity.getId();
    }
}
