import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        if ("malina@example.com".equals(username) && "password".equals(password)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful"));
            return "AdminDashboard?faces-redirect=true";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Please try again."));
            return null;
        }
    }
}
