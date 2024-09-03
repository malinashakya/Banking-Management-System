/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.api;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.CustomerRepository;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerRESTAPI {

    @Inject
    private CustomerRepository customerRepository;

    @GET
    public Response getAllCustomers() {
        try {
            List<Customer> customers = customerRepository.getAll();
            return RestResponse.responseBuilder("true", "200", "Customers retrieved successfully", customers.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        try {
            Customer customer = customerRepository.getById(id);
            if (customer != null) {
                return RestResponse.responseBuilder("true", "200", "Customer found", customer.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Customer not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @POST
    public Response createCustomer(Customer customer) {
        try {
            customerRepository.save(customer);
            return RestResponse.responseBuilder("true", "201", "Customer created successfully", customer.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "400", "Failed to create customer", e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        try {
            Customer existingCustomer = customerRepository.getById(id);
            if (existingCustomer != null) {
                customer.setId(id);
                customerRepository.update(customer);
                return RestResponse.responseBuilder("true", "200", "Customer updated successfully", customer.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Customer not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        try {
            Customer customer = customerRepository.getById(id);
            if (customer != null) {
                customerRepository.delete(id);
                return RestResponse.responseBuilder("true", "204", "Customer deleted successfully", null);
            } else {
                return RestResponse.responseBuilder("false", "404", "Customer not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }
}

