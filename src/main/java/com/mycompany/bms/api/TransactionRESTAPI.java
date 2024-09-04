/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.api;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.repository.TransactionRepository;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionRESTAPI {

    @Inject
    private TransactionRepository transactionRepository;

    @GET
    public Response getAllTransactions() {
        try {
            List<Transaction> transactions = transactionRepository.getAll();
            return RestResponse.responseBuilder("true", "200", "Transactions retrieved successfully", transactions.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response getTransactionById(@PathParam("id") Long id) {
        try {
            Transaction transaction = transactionRepository.getById(id);
            if (transaction != null) {
                return RestResponse.responseBuilder("true", "200", "Transaction found", transaction.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Transaction not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @POST
    public Response createTransaction(Transaction transaction) {
        try {
            transactionRepository.save(transaction);
            return RestResponse.responseBuilder("true", "201", "Transaction created successfully", transaction.toString());
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "400", "Failed to create transaction", e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTransaction(@PathParam("id") Long id, Transaction transaction) {
        try {
            Transaction existingTransaction = transactionRepository.getById(id);
            if (existingTransaction != null) {
                transaction.setId(id);
                transactionRepository.update(transaction);
                return RestResponse.responseBuilder("true", "200", "Transaction updated successfully", transaction.toString());
            } else {
                return RestResponse.responseBuilder("false", "404", "Transaction not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTransaction(@PathParam("id") Long id) {
        try {
            Transaction transaction = transactionRepository.getById(id);
            if (transaction != null) {
                transactionRepository.delete(id);
                return RestResponse.responseBuilder("true", "204", "Transaction deleted successfully", null);
            } else {
                return RestResponse.responseBuilder("false", "404", "Transaction not found", null);
            }
        } catch (Exception e) {
            return RestResponse.responseBuilder("false", "500", "An error occurred", e.getMessage());
        }
    }
}

//{
//  "account": {
//    "id": "2"
//  },
//  "transactionType": "WITHDRAW",
//  "amount": "250"
//}