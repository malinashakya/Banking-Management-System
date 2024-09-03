package com.mycompany.bms.api;

import com.mycompany.bms.model.Admin;
import com.mycompany.bms.repository.AdminRepository;
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
        List<Admin> admins = adminRepository.getAll();
        return Response.ok(admins).build();
    }

    @GET
    @Path("/{id}")
    public Response getAdminById(@PathParam("id") Long id) {
        Admin admin = adminRepository.getById(id);
        if (admin != null) {
            return Response.ok(admin).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createAdmin(Admin admin) {
        try {
            adminRepository.save(admin);
            return Response.status(Response.Status.CREATED).entity(admin).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAdmin(@PathParam("id") Long id, Admin admin) {
        Admin existingAdmin = adminRepository.getById(id);
        if (existingAdmin != null) {
            admin.setId(id);
            adminRepository.update(admin);
            return Response.ok(admin).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAdmin(@PathParam("id") Long id) {
        Admin admin = adminRepository.getById(id);
        if (admin != null) {
            adminRepository.delete(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
