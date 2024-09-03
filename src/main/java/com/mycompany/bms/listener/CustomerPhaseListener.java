package com.mycompany.bms.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.faces.application.FacesMessage;

public class CustomerPhaseListener implements PhaseListener {

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        String requestURI = context.getExternalContext().getRequestContextPath() + context.getExternalContext().getRequestServletPath();
        String loginURI = context.getExternalContext().getRequestContextPath() + "/Customer/CustomerLogin.xhtml";

        boolean loggedIn = (session != null && session.getAttribute("loggedInCustomer") != null);
        boolean loginRequest = requestURI.equals(loginURI);
        boolean customerRequest = requestURI.startsWith(context.getExternalContext().getRequestContextPath() + "/Customer/");

        if (!loggedIn && customerRequest && !loginRequest) {
            try {
                context.getExternalContext().redirect(loginURI);
            } catch (IOException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Redirect Error", "An error occurred while redirecting to the login page."));
            }
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        // No action needed here
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE; // Adjust if needed
    }
}
