package com.mycompany.bms.bean;

import com.mycompany.bms.model.Customer;
import com.mycompany.bms.repository.CustomerRepository;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("profileBean")
@SessionScoped
public class ProfileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private SessionCustomerBean sessionCustomerBean;

    private Customer loggedInCustomer;

    @PostConstruct
    public void init() {
        loggedInCustomer = sessionCustomerBean.getCurrentCustomer();
        if (loggedInCustomer == null) {
            // Redirect to login if not logged in
            sessionCustomerBean.checkSession();
            return; // Exit early to prevent further initialization
        }
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public void updateProfile() {
        if (loggedInCustomer != null) {
            try {
                customerRepository.save(loggedInCustomer);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Updated", "Your profile has been updated successfully."));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update profile: " + e.getMessage()));
            }
        }
    }
}
