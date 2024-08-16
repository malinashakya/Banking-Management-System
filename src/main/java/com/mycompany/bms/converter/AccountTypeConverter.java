
package com.mycompany.bms.converter;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.repository.AccountTypeRepository;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(forClass = AccountType.class)
public class AccountTypeConverter implements Converter {

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.parseLong(value);
            return (AccountType) accountTypeRepository.getById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof AccountType) {
            AccountType accountType = (AccountType) object;
            return String.valueOf(accountType.getId()); // Ensure `getId` returns the correct type
        } else {
            throw new IllegalArgumentException("Object is not of type AccountType");
        }
    }
}

