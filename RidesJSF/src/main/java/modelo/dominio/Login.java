package modelo.dominio;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usernameOrEmail;

    private String password;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Driver driver;

    public Login() {
        super();
    }

    public Login(String usernameOrEmail, String password, Driver driver) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Validates the login credentials.
     * @param usernameOrEmail The username or email provided.
     * @param password The password provided.
     * @return true if credentials match, false otherwise.
     */
    public boolean validateLogin(String usernameOrEmail, String password) {
        return this.usernameOrEmail.equals(usernameOrEmail) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return "Login [usernameOrEmail=" + usernameOrEmail + ", driver=" + driver + "]";
    }
}
