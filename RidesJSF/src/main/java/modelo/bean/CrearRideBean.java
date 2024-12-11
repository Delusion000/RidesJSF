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
		LoginBean loggedUser = (LoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("loggedUser");
		if (loggedUser != null) {
			this.driverName = loggedUser.getName();
			this.driverEmail = loggedUser.getEmail();
		}

	}

	public void createRide() {
		try {
			if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Los campos 'Desde' y 'Hasta' son obligatorios.", null));
				return;
			}

			if (!isValidText(from) || !isValidText(to)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Los campos 'Desde' y 'Hasta' deben contener solo letras.", null));
				return;
			}

			if (fecha == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha es obligatoria.", null));
				return;
			}
			if (!isValidNumber(String.valueOf(nPlaces), false)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El número de plazas debe ser un valor numérico entero.", null));
				return;
			}

			if (!isValidNumber(String.valueOf(price), true)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El precio debe ser un valor numérico válido (puede incluir decimales).", null));
				return;
			}

			if (nPlaces <= 0 || nPlaces > 53) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El número de plazas debe ser mayor que 0 y capacidad maxima de 53.", null));
				return;
			}

			if (price <= 0 || price > 50) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El precio debe ser mayor que 0 y menor a 50 euros.", null));
				return;
			}

			Ride ride = dataAccess.createRide(from, to, fecha, nPlaces, price, driverEmail);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Viaje creado con éxito, número de viaje: " + ride.getRideNumber(), null));
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

	private boolean isValidNumber(String str, boolean allowDecimal) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		String regex = allowDecimal ? "\\d+(\\.\\d+)?" : "\\d+";
		return str.matches(regex);
	}

	private boolean isValidText(String text) {
		String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"; // Permite letras, acentos, ñ y espacios
		return text != null && text.matches(regex);
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

}
