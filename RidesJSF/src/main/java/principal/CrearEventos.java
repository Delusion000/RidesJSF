package principal;

import javax.persistence.EntityManager;
import modelo.JPAUtil;
import modelo.dominio.EventoLogin;
import java.util.*;

public class CrearEventos {

	public CrearEventos() {
	}

	private void createAndStoreEventoLogin(Long id, String descripcion, Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			EventoLogin e = new EventoLogin();
			e.setId(id);
			e.setDescripcion(descripcion);
			e.setFecha(fecha);
			em.persist(e);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private List<EventoLogin> listaEventos() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<EventoLogin> result = em.createQuery("from EventoLogin", EventoLogin.class).getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public static void main(String[] args) {
		CrearEventos e = new CrearEventos();

		System.out.println("Creación de eventos:");
		e.createAndStoreEventoLogin(1L, "Pepe ha hecho login correctamente", new Date());
		e.createAndStoreEventoLogin(2L, "Nerea ha intentado hacer login", new Date());
		e.createAndStoreEventoLogin(3L, "Kepa ha hecho login correctamente", new Date());

		System.out.println("Listado de eventos:");
		List<EventoLogin> eventos = e.listaEventos();

		for (EventoLogin ev : eventos) {
			System.out
					.println("Id: " + ev.getId() + " Descripcion: " + ev.getDescripcion() + " Fecha: " + ev.getFecha());
		}
	}
}
