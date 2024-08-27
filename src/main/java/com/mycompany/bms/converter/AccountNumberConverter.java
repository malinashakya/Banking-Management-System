/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.converter;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.repository.AccountRepository;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author malina
 */
@FacesConverter("accountNumberConverter")
public class AccountNumberConverter implements Converter {
    @Inject
    private AccountRepository accountRepository; // Inject your repository

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return accountRepository.findByAccountNumber(value); // Convert account number to account entity
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Account) {
            return ((Account) value).getAccountNumber(); // Return account number
        }
        throw new IllegalArgumentException("Object is not of type Account");
    }
}

