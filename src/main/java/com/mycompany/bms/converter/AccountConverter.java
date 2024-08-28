/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.converter;

/**
 *
 * @author malina
 */

import com.mycompany.bms.model.Account;
import com.mycompany.bms.repository.AccountRepository;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter("accountConverter")
public class AccountConverter implements Converter<Account> {
    @Inject
    private AccountRepository accountRepository;

    @Override
    public Account getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return accountRepository.findByAccountNumber(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Account account) {
        if (account == null) {
            return "";
        }
        return account.getAccountNumber();
    }
}
