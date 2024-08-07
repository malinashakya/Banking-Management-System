package com.mycompany.bms.repository.impl;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import com.mycompany.bms.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AdminRepositoryImpl implements AdminRepository {
    @Override
    public Admin findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Admin.class, id);
        }
    }

    @Override
    public List<Admin> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Admin", Admin.class).list();
        }
    }

    @Override
    public void save(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(admin);
            transaction.commit();
        }
    }

    @Override
    public void update(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(admin);
            transaction.commit();
        }
    }

    @Override
    public void delete(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(admin);
            transaction.commit();
        }
    }
}
