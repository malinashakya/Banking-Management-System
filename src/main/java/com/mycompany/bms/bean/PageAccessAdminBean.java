package com.mycompany.bms.bean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

public class PageAccessAdminBean implements Serializable {
       private static final long serialVersionUID = 1L;

    // Check if the user is logged in and redirect if not
    public void checkLoginStatus() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        
        // Check if there is a logged-in admin in the session
        Object loggedInAdmin = session != null ? session.getAttribute("loggedInAdmin") : null;

        if (loggedInAdmin == null) {
            try {
                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Admin/loginpage.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        
        // Check if there is a logged-in admin in the session
        Object loggedInAdmin = session != null ? session.getAttribute("loggedInAdmin") : null;
        return loggedInAdmin != null;
    }
}
