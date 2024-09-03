package com.mycompany.bms.bean;

import com.mycompany.bms.model.Customer;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * Manages session data for customers, including storing and retrieving customer
 * information, checking session validity, and handling logout.
 */
@Named("sessionCustomerBean")
@SessionScoped
public class SessionCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Retrieves the currently logged-in customer from the session.
     *
     * @return The current customer, or null if no customer is logged in.
     */
    public Customer getCurrentCustomer() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (session != null) ? (Customer) session.getAttribute("loggedInCustomer") : null;
    }

    /**
     * Stores the customer object in the session.
     *
     * @param customer The customer to store in the session.
     */
    public void storeCustomerInSession(Customer customer) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("loggedInCustomer", customer);
    }

    /**
     * Logs out the current customer by invalidating the session.
     */
    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate();  // Invalidate the session
        }

        redirectToLogin();
    }

    /**
     * Redirects to the login page.
     */
    void redirectToLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Customer/CustomerLogin.xhtml");
        } catch (IOException e) {
            // Log the exception (you might use a logger instead of printing the stack trace in a real application)
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Redirect Error", "An error occurred while redirecting to the login page."));
        }
    }
}
