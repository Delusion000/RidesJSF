package modelo.bean;

import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelo.dominio.Ride;
import principal.HibernateDataAccess;

import java.io.Serializable;
import java.util.Date;

@Named("CrearRideBean")
@SessionScoped
public class CrearRideBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String from;
	private String to;
	private Date fecha;
	private int nPlaces;
	private float price;
	private String driverEmail;
	private String driverName;

	@Inject
	private HibernateDataAccess dataAccess;

	public CrearRideBean() {
		// Inicialización del objeto DAO (HibernateDataAccess)
		LoginBean loggedUser = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("loggedUser");
		if (loggedUser != null) {
			this.driverName = loggedUser.getName();
			this.driverEmail = loggedUser.getEmail();
		}

	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	// Getters and setters for the properties
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getnPlaces() {
		return nPlaces;
	}

	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public void createRide() {
        try {
            if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los campos 'Desde' y 'Hasta' son obligatorios.", null));
                return;
            }

            if (fecha == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha es obligatoria.", null));
                return;
            }

            if (nPlaces <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "El número de plazas debe ser mayor que 0.", null));
                return;
            }

            if (price <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "El precio debe ser mayor que 0.", null));
                return;
            }

            Ride ride = dataAccess.createRide(from, to, fecha, nPlaces, price, driverEmail);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Viaje creado con éxito, número de viaje: " + ride.getRideNumber(), null));
        } catch (RideAlreadyExistException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        } catch (RideMustBeLaterThanTodayException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado: " + e.getMessage(), null));
        }
    }
}


