package modelo.dominio;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class EventoLogin {
	@Id
	private Long id;
	private String descripcion;
	private Date fecha;

	public EventoLogin() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
