/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.api;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.repository.AccountTypeRepository;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accountTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountTypeRESTAPI {

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @GET
    public Response getAllAccountTypes() {
        try {
            List<AccountType> accountTypes = accountTypeRepository.getAll();
            return RestResponse.responseBuilder("true", "200", "AccountTypes retrieved successfully", accountTypes.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getAccountTypeById(@PathParam("id") Long id) {
        try {
            AccountType accountType = accountTypeRepository.getById(id);
            if (accountType != null) {
                return RestResponse.responseBuilder("true", "200", "AccountType found", accountType.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "AccountType not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @POST
    public Response createAccountType(AccountType accountType) {
        try {
            accountTypeRepository.save(accountType);
            return RestResponse.responseBuilder("true", "201", "AccountType created successfully", accountType.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "400", "Failed to create accountType", e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAccountType(@PathParam("id") Long id, AccountType accountType) {
        try {
            AccountType existingAccountType = accountTypeRepository.getById(id);
            if (existingAccountType != null) {
                accountType.setId(id);
                accountTypeRepository.update(accountType);
                return RestResponse.responseBuilder("true", "200", "AccountType updated successfully", accountType.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "AccountType not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccountType(@PathParam("id") Long id) {
        try {
            AccountType accountType = accountTypeRepository.getById(id);
            if (accountType != null) {
                accountTypeRepository.delete(id);
                return RestResponse.responseBuilder("true", "204", "AccountType deleted successfully", null);
            } else {
                return RestResponse.responseBuilder("false", "404", "AccountType not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }
}

