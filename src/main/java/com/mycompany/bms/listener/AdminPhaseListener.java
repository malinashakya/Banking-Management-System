/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.listener;

/**
 *
 * @author malina
 */
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class AdminPhaseListener implements PhaseListener {

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        String requestURI = context.getExternalContext().getRequestContextPath() + context.getExternalContext().getRequestServletPath();
        String loginURI = context.getExternalContext().getRequestContextPath() + "/Admin/loginpage.xhtml";
        String registerURI = context.getExternalContext().getRequestContextPath() + "/Admin/RegisterAdmin.xhtml";

        boolean loggedIn = (session != null && session.getAttribute("loggedInAdmin") != null);
        boolean loginRequest = requestURI.equals(loginURI);
        boolean registerRequest = requestURI.equals(registerURI);

        boolean adminRequest = requestURI.startsWith(context.getExternalContext().getRequestContextPath() + "/Admin/");

        if (!loggedIn && adminRequest && !loginRequest && !registerRequest) {
            try {
                context.getExternalContext().redirect(loginURI);
            } catch (IOException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Redirect Error", "An error occurred while redirecting to the login page."));
            }
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        // No-op
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
