import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class LoginBean {
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

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (username.equals("admin@example.com") && password.equals("password")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful"));
            // Add navigation or additional logic here
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password"));
        }
    }
}
