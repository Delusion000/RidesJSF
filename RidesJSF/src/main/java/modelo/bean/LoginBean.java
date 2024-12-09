package modelo.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.regex.Pattern;

import principal.HibernateDataAccess;

@Named("LoginBean")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private String name;

	@Inject
	private HibernateDataAccess dataAccess; // Inyecta la dependencia

	public String login() {
		try {
			if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
	            FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Los campos 'Email' y 'Contraseña' son obligatorios.", null));
	            return null; // Mantente en la página de inicio de sesión
	        }
			if (dataAccess.validateDriver(email, password)) {
				// Obtén el nombre del usuario logueado
				this.name = dataAccess.getDriverNameByEmail(email);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedUser", this);
				System.out.println("Inicio de sesión exitoso");
				return "home"; // Redirige a la página principal
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas", null));
				System.out.println("Credenciales incorrectas");
				return "Login.xhtml"; // Mantente en la página de inicio de sesión
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Redirige a una página de error
		}
	}

	public String register() {
		try {
			// Validar la contraseña
			if (!isValidPassword(password)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La contraseña debe tener al menos 8 caracteres y un número", null));
				return "register";
			}

			// Intentar registrar al usuario
			if (dataAccess.addNewDriver(email, name, password)) {
				System.out.println("Registro exitoso");
				return "Login.xhtml"; // Redirige a la página de inicio de sesión
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo ya está en uso", null));
				System.out.println("Error: el correo ya existe");
				return "register"; // Mantente en la página de registro
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el servidor", null));
			e.printStackTrace();
			return "error";
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "Login"; // Redirección explícita
	}

	// Validador de contraseña
	private boolean isValidPassword(String password) {
		String regex = "^(?=.*\\d).{8,}$"; // Al menos un número y 8 caracteres
		return Pattern.matches(regex, password);
	}

	// Getters y setters para propiedades de vista
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
