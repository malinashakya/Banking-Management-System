package com.mycompany.bms.bean;

import com.mycompany.bms.model.Admin;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * Manages session data for admins, including storing and retrieving admin information,
 * checking session validity, and handling logout.
 */
@Named("sessionAdminBean")
@SessionScoped
public class SessionAdminBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Retrieves the currently logged-in admin from the session.
     * @return The current admin, or null if no admin is logged in.
     */
    public Admin getCurrentAdmin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (session != null) ? (Admin) session.getAttribute("loggedInAdmin") : null;
    }

    /**
     * Stores the admin object in the session.
     * @param admin The admin to store in the session.
     */
    public void storeAdminInSession(Admin admin) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("loggedInAdmin", admin);
    }

    /**
     * Logs out the current admin by invalidating the session.
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
    private void redirectToLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/Admin/loginpage.xhtml");
        } catch (IOException e) {
            // Log the exception (you might use a logger instead of printing the stack trace in a real application)
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Redirect Error", "An error occurred while redirecting to the login page."));
        }
    }
}
