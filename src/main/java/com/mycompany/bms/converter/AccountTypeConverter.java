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
@FacesConverter(value = "accountTypeConverter")
public class AccountTypeConverter implements Converter, Serializable {

    @Inject
    private AccountTypeRepository accountTypeRepository;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || value.length() == 0 || value.equals("")) {
            return null;
        }
        Long id = Long.valueOf(value);
        AccountType accountType = accountTypeRepository.getById(Long.valueOf(value));
        System.out.println("Converting String to AccountType: " + id + " -> " + accountType);
        return accountType;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null || object.equals("")) {
            return "";
        }
        if (object instanceof AccountType) {
            String id = String.valueOf(((AccountType) object).getId());
            System.out.println("Converting AccountType to String: " + object + " -> " + id);

            return String.valueOf(((AccountType) object).getId());
        } else {
            throw new IllegalArgumentException("Object is not of type AccountType");
        }
    }

}
