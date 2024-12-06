package modelo.bean;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;


@Named("homeBean")
@SessionScoped
public class HomeBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driverName;

    public HomeBean() {
        // Recuperar el usuario logueado desde la sesión
       LoginBean loggedUser = (LoginBean) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loggedUser");
        if (loggedUser != null) {
            this.driverName = loggedUser.getName();
        }
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String createRide() {
        return "CrearRide";
    }

    public String queryRides() {
        return "QueryRides";
    }

    
}
