package com.mycompany.bms;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateTest {
    public static void main(String[] args) {
        // Create a new Admin object
        Admin admin = new Admin("john_doe", "password123", "John", "Doe", "123 Elm St", 1234567890);

        // Save the Admin object to the database
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(admin);
            transaction.commit();
            System.out.println("Admin saved successfully!");

            // Retrieve the Admin object from the database
            Admin retrievedAdmin = session.get(Admin.class, admin.getId());
            System.out.println("Retrieved Admin: " + retrievedAdmin.getUsername());

            // Update the Admin object
            transaction = session.beginTransaction();
            retrievedAdmin.setAddress("456 Oak St");
            session.update(retrievedAdmin);
            transaction.commit();
            System.out.println("Admin updated successfully!");

            // Delete the Admin object
            transaction = session.beginTransaction();
            session.delete(retrievedAdmin);
            transaction.commit();
            System.out.println("Admin deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
