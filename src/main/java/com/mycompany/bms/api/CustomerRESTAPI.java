/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.api;

/**
 *
 * @author malina
 */
import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.CustomerRepository;
import java.util.ArrayList;
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
            // Retrieve the existing customer from the repository
            Customer existingCustomer = customerRepository.getById(id);
            if (existingCustomer != null) {
                // Ensure the existing accounts list is not null
                if (existingCustomer.getAccounts() == null) {
                    existingCustomer.setAccounts(new ArrayList<>()); // Initialize if null
                }

                // Detach existing accounts
                List<Account> detachedAccounts = new ArrayList<>(existingCustomer.getAccounts());
                existingCustomer.getAccounts().clear();

                // Update customer details
                existingCustomer.setFirstName(customer.getFirstName());
                existingCustomer.setLastName(customer.getLastName());
                existingCustomer.setAddress(customer.getAddress());
                existingCustomer.setContact(customer.getContact());
                existingCustomer.setUsername(customer.getUsername());
                existingCustomer.setPassword(customer.getPassword());

                // Ensure the incoming accounts list is not null
                if (customer.getAccounts() != null) {
                    for (Account account : customer.getAccounts()) {
                        // If the account is already in detachedAccounts, update its details
                        if (detachedAccounts.contains(account)) {
                            Account existingAccount = detachedAccounts.get(detachedAccounts.indexOf(account));
                            existingAccount.setAccountType(account.getAccountType());
                            existingAccount.setBalance(account.getBalance());
                            // Set any other account fields that need to be updated
                            existingCustomer.getAccounts().add(existingAccount);
                        } else {
                            // If it's a new account, add it and set the customer association
                            account.setCustomer(existingCustomer);
                            existingCustomer.getAccounts().add(account);
                        }
                    }
                }

                // Persist the updated customer entity
                customerRepository.update(existingCustomer);
                return RestResponse.responseBuilder("true", "200", "Customer updated successfully", existingCustomer.toString());
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
