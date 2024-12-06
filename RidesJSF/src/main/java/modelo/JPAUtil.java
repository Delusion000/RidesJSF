package modelo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class JPAUtil {
	private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

	private static EntityManagerFactory buildEntityManagerFactory() {
		try {
			return Persistence.createEntityManagerFactory("miUnidadPersistencia");
		} catch (Throwable ex) {
			System.err.println("Initial EntityManagerFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
}