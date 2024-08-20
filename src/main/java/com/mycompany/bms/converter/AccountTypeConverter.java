package com.mycompany.bms.converter;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.repository.AccountTypeRepository;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@RequestScoped
@FacesConverter(value = "accountTypeConverter", forClass = AccountType.class, managed = true)
public class AccountTypeConverter implements Converter, Serializable {

    @Inject
    private AccountTypeRepository accountTypeRepository;

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
