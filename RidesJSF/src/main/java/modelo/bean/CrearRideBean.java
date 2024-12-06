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

	// Method to create the ride
//	public String createRide() {
//	    try {
//	        // Llamar al método de HibernateDataAccess para crear el viaje
//	        Ride ride = dataAccess.createRide(from, to, fecha, nPlaces, price, driverEmail);
//	        
//	        // Si el viaje se crea correctamente, no redirigir, solo quedarse en la misma página
//	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Viaje creado exitosamente", null));
//	        
//	    } catch (RideMustBeLaterThanTodayException e) {
//	        // Si ocurre una excepción relacionada con la fecha
//	        FacesContext.getCurrentInstance().addMessage(null, 
//	                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
//	    } catch (RideAlreadyExistException e) {
//	        // Si ya existe un viaje con las mismas características
//	        FacesContext.getCurrentInstance().addMessage(null, 
//	                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
//	    } catch (Exception e) {
//	        // En caso de excepción inesperada
//	        FacesContext.getCurrentInstance().addMessage(null, 
//	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado al crear el viaje", null));
//	    }
//	    
//	    // Si ocurre un error o la operación es exitosa, mantener al usuario en la misma página
//	    return null;
//	}
	public void createRide() {
		try {
			Ride ride = dataAccess.createRide(from, to, fecha, nPlaces, price, driverEmail);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Viaje creado con éxito, numero de viaje: " + ride.getRideNumber(), null));
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
