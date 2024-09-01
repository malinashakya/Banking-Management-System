package com.mycompany.bms.repository;

import com.mycompany.bms.model.Session_Customer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

public class SessionCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Session_Customer session) {
        entityManager.persist(session);
    }

    public Session_Customer findActiveSessionByCustomer(Long customerId) {
        TypedQuery<Session_Customer> query = entityManager.createQuery(
            "SELECT s FROM Session_Customer s WHERE s.customer.id = :customerId AND s.logoutTime IS NULL", Session_Customer.class);
        query.setParameter("customerId", customerId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
