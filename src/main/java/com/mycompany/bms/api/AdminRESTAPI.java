package com.mycompany.bms.api;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
import com.mycompany.bms.util.JwtUtil;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/admins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminRESTAPI {

    @Inject
    private AdminRepository adminRepository;

    @GET
    public Response getAllAdmins() {
        try {
            List<Admin> admins = adminRepository.getAll();
            return RestResponse.responseBuilder("true", "200", "Admins retrieved successfully", admins.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getAdminById(@PathParam("id") Long id) {
        try {
            Admin admin = adminRepository.getById(id);
            if (admin != null) {
                return RestResponse.responseBuilder("true", "200", "Admin found", admin.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Admin not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @POST
    public Response createAdmin(Admin admin) {
        try {
            adminRepository.save(admin);
            return RestResponse.responseBuilder("true", "201", "Admin created successfully", admin.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "400", "Failed to create admin", e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAdmin(@PathParam("id") Long id, Admin admin) {
        try {
            Admin existingAdmin = adminRepository.getById(id);
            if (existingAdmin != null) {
                admin.setId(id);
                adminRepository.update(admin);
                return RestResponse.responseBuilder("true", "200", "Admin updated successfully", admin.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Admin not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAdmin(@PathParam("id") Long id) {
        try {
            Admin admin = adminRepository.getById(id);
            if (admin != null) {
                adminRepository.delete(id);
                return RestResponse.responseBuilder("true", "204", "Admin deleted successfully", null);
            } else {
                return RestResponse.responseBuilder("false", "404", "Admin not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    // Login API
    @POST
    @Path("/login")
    public Response login(Admin loginDetails) {
        try {
            Admin admin = adminRepository.authenticate(loginDetails.getUsername(), loginDetails.getPassword());
            if (admin != null) {
                // Generate JWT token
                String token = JwtUtil.generateToken(admin.getUsername());
                String result = "{\"username\": \"" + admin.getUsername() + "\", \"token\": \"" + token + "\"}";
                return RestResponse.responseBuilder("true", "200", "Login successful", result);
            } else {
                return RestResponse.responseBuilder("false", "401", "Invalid credentials", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred: " + e.getMessage(), null);
        }
    }

    //Just for test purpose
    @GET
    @Path("greetings")
    public Response getGreetings() {
        return RestResponse.responseBuilder("true", "200", "Greeted", "Hello everyone");
    }
}
