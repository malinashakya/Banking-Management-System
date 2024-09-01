package com.mycompany.bms.bean;

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.model.Session_Customer;
import com.mycompany.bms.repository.SessionCustomerRepository;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.util.Date;

@Named
@RequestScoped
public class LogoutCustomerBean {

    @Inject
    private SessionCustomerRepository sessionCustomerRepository;

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Long customerId = getLoggedInCustomerId(); // Retrieve logged-in customer ID

        if (customerId != null) {
            Session_Customer session = sessionCustomerRepository.findActiveSessionByCustomer(customerId);
            if (session != null) {
                session.setLogoutTime(new Date());
                sessionCustomerRepository.save(session);
            }
        }

        facesContext.getExternalContext().invalidateSession(); // Invalidate the HTTP session
        try {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Customer/CustomerLogin.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getLoggedInCustomerId() {
        Customer customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInCustomer");
        return customer != null ? customer.getId() : null;
    }
}
