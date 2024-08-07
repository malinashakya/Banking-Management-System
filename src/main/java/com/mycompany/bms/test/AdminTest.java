package com.mycompany.bms.test;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.service.AdminService;

public class AdminTest {

    public static void main(String[] args) {
        AdminService adminService = new AdminService();

        // Create a new Admin
//        Admin newAdmin = new Admin("malina@example.com", "password123", "Malina", "Shakya", "KTM", 1234567890);
//        adminService.save(newAdmin);
//        System.out.println("Admin saved: " + newAdmin);

        // Read all Admins
        System.out.println("All Admins:");
        adminService.getAll().forEach(admin -> System.out.println(admin.getId() + ": " + admin.getUsername()));
//
//        // Update an Admin (assuming the ID is known and exists in the database)
//        Admin adminToUpdate = adminService.getAll().get(0); // Get the first Admin for update
//        adminToUpdate.setPassword("newpassword123");
//        adminService.update(adminToUpdate);
//        System.out.println("Admin updated: " + adminToUpdate);

        // Delete an Admin (assuming the ID is known and exists in the database)
        Long adminIdToDelete = (long)4; // Use the ID of the updated Admin
        adminService.delete(adminIdToDelete);
        System.out.println("Admin deleted with ID: " + adminIdToDelete);
        
                // Read all Admins
        System.out.println("All Admins:");
        adminService.getAll().forEach(admin -> System.out.println(admin.getId() + ": " + admin.getUsername()));
    }
}
