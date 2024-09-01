/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.bean;

/**
 *
 * @author malina
 */
import com.mycompany.bms.model.Admin;
import com.mycompany.bms.model.Session_Admin;
import com.mycompany.bms.repository.SessionAdminRepository;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.util.Date;

@Named
@RequestScoped
public class LogoutAdminBean {

    @Inject
    private SessionAdminRepository sessionAdminRepository;

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Long adminId = getLoggedInAdminId(); // Retrieve logged-in admin ID

        if (adminId != null) {
            Session_Admin session = sessionAdminRepository.findActiveSessionByAdmin(adminId);
            if (session != null) {
                session.setLogoutTime(new Date());
                sessionAdminRepository.save(session);
            }
        }

        facesContext.getExternalContext().invalidateSession(); // Invalidate the HTTP session
        try {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Admin/loginpage.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getLoggedInAdminId() {
        Admin admin = (Admin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInAdmin");
        return admin != null ? admin.getId() : null;
    }
}

