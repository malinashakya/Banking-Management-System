/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.repository;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.Session_Admin;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

public class SessionAdminRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Session_Admin session) {
        entityManager.persist(session);
    }

    public Session_Admin findActiveSessionByAdmin(Long adminId) {
        TypedQuery<Session_Admin> query = entityManager.createQuery(
            "SELECT s FROM Session_Admin s WHERE s.admin.id = :adminId AND s.logoutTime IS NULL", Session_Admin.class);
        query.setParameter("adminId", adminId);
        return query.getResultStream().findFirst().orElse(null);
    }
}
