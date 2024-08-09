/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.test;

/**
 *
 * @author malina
 */
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.mycompany.bms.model.Admin;

public class PersistenceTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankingDS");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Admin admin = new Admin("Test User", "testuser", "password");
            em.persist(admin);

            Admin foundAdmin = em.find(Admin.class, admin.getId());
            System.out.println("Found Admin: " + foundAdmin.getUsername());

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
