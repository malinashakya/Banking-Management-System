package com.mycompany.bms.bean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PageAccessBean {

    // Check if the user is logged in and redirect if not
    public void checkLoginStatus() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        
        // Check if there is a logged-in customer in the session
        Object loggedInCustomer = session != null ? session.getAttribute("loggedInCustomer") : null;

        if (loggedInCustomer == null) {
            try {
                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Customer/CustomerLogin.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        
        // Check if there is a logged-in customer in the session
        Object loggedInCustomer = session != null ? session.getAttribute("loggedInCustomer") : null;
        return loggedInCustomer != null;
    }
}
