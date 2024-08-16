
package com.mycompany.bms.converter;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.repository.AccountTypeRepository;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter(value = "accountTypeConverter")
public class AccountTypeConverter implements Converter {

    private AccountTypeRepository accountTypeRepository;

    public AccountTypeConverter() {
        try {
            InitialContext ctx = new InitialContext();
            accountTypeRepository = (AccountTypeRepository) ctx.lookup("java:module/AccountTypeRepository");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

   @Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
        return null;
    }
    Long id = Long.valueOf(value);
    AccountType accountType = accountTypeRepository.getById(id);
    System.out.println("Converting String to AccountType: " + id + " -> " + accountType);
    return accountType;
}

@Override
public String getAsString(FacesContext context, UIComponent component, Object object) {
    if (object == null) {
        return "";
    }
    if (object instanceof AccountType) {
        String id = String.valueOf(((AccountType) object).getId());
        System.out.println("Converting AccountType to String: " + object + " -> " + id);
        
        return id;
    } else {
        throw new IllegalArgumentException("Object is not of type AccountType");
    }
}

}

