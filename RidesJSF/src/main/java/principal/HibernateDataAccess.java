package principal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import jakarta.enterprise.context.ApplicationScoped;

import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import modelo.JPAUtil;
import modelo.dominio.Driver;
import modelo.dominio.Ride;

@ApplicationScoped
public class HibernateDataAccess {

	public HibernateDataAccess() {

	}

	public void initializeDatabase() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			// Ejemplo de creación de datos iniciales
			Driver driver1 = new Driver("admin@example.com", "admin", "admin1");
			Ride ride1 = driver1.addRide("CityA", "CityB", new Date(), 3, 15.0f);
			Ride ride2 = driver1.addRide("CityC", "CityD", new Date(), 5, 11.0f);

			Driver driver2 = new Driver("Test2@example.com", "example", "example1");
			Ride ride3 = driver2.addRide("CityE", "CityF", new Date(), 2, 12.0f);

			// Persistir datos
			em.persist(driver1);
			em.persist(driver2);
			em.persist(ride1);
			em.persist(ride2);
			em.persist(ride3);

			em.getTransaction().commit();
			System.out.println("Datos iniciales precargados en la base de datos.");
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public boolean validateDriver(String email, String password) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			String jpql = "SELECT COUNT(d) FROM Driver d WHERE d.email = :email AND d.password = :password";
			Query query = em.createQuery(jpql);
			query.setParameter("email", email);
			query.setParameter("password", password);

			Long count = (Long) query.getSingleResult();
			return count > 0; // Devuelve true si encontró un registro.
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// metodo de guardar nuevo Driver
	public boolean addNewDriver(String correo, String name, String password) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			// Verificar si ya existe un Driver con el correo proporcionado
			Driver existingDriver = em.find(Driver.class, correo);
			if (existingDriver != null) {
				System.out.println("Driver ya existe con el correo: " + correo);
				return false; // Ya existe, no se puede crear
			}

			// Crear y guardar el nuevo Driver
			Driver driver = new Driver(correo, name, password);
			em.getTransaction().begin();
			em.persist(driver);
			em.getTransaction().commit();
			System.out.println("Driver creado con éxito");
			return true; // Éxito
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false; // Error
		} finally {
			em.close();
		}
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		EntityManager em = JPAUtil.getEntityManager();

		System.out.println(
				">> DataAccess: createRide => from=" + from + " to=" + to + " driver=" + driverEmail + " date=" + date);

		try {
			// Validar la fecha ingresada
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException("El viaje debe ser posterior a hoy");
			}

			em.getTransaction().begin();

			// Buscar al conductor por email
			Driver driver = em.find(Driver.class, driverEmail);
			if (driver == null) {
				throw new IllegalArgumentException("No se encontró un conductor con el correo proporcionado.");
			}

			// Verificar si el viaje ya existe
			if (driver.doesRideExist(em, driverEmail, from, to, date)) {
				em.getTransaction().rollback(); // No es necesario continuar con la transacción
				throw new RideAlreadyExistException("Ya existe un viaje con las mismas características.");
			}

			// Crear y persistir el viaje
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			em.persist(ride); // Actualizar el conductor con el nuevo viaje

			em.getTransaction().commit();
			return ride; // Retorna el viaje creado exitosamente

		} catch (RideAlreadyExistException | RideMustBeLaterThanTodayException e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw e; // Lanzar excepciones específicas para que el bean las maneje
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RuntimeException("Error inesperado al crear el viaje", e);
		} finally {
			em.close();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		EntityManager em = JPAUtil.getEntityManager();
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		System.out.println("Ciudades de partida obtenidas: " + cities);
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		EntityManager em = JPAUtil.getEntityManager();
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;
	}

	public List<Ride> getRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND DATE(r.date) = ?3",
				Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, new java.sql.Date(date.getTime()));
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
	    System.out.print("search ejecutado");
		}
		return res;
	}
//	public List<Ride> getRides(String from, String to, Date date) {
//	    List<Ride> res = new ArrayList<>();
//	    EntityManager em = null;
//
//	    try {
//	        // Verificación de valores nulos o vacíos
//	        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty() || date == null) {
//	            throw new IllegalArgumentException("Los parámetros 'from', 'to' y 'date' no pueden ser nulos o vacíos.");
//	        }
//
//	        em = JPAUtil.getEntityManager();
//	        System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);
//
//	        TypedQuery<Ride> query = em.createQuery(
//	            "SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND DATE(r.date) = ?3", Ride.class
//	        );
//	        query.setParameter(1, from);
//	        query.setParameter(2, to);
//	        query.setParameter(3, new java.sql.Date(date.getTime()));
//
//	        List<Ride> rides = query.getResultList();
//	        for (Ride ride : rides) {
//	            res.add(ride);
//	            System.out.println("Ride encontrado: " + ride);
//	        }
//	    } catch (IllegalArgumentException e) {
//	    	if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
//	    	System.err.println("Error: " + e.getMessage());
//			throw e;
//	        
//	    } catch (Exception e) {
//	        System.err.println("Ocurrió un error inesperado: " + e.getMessage());
//	    } finally {
//	        if (em != null && em.isOpen()) {
//	            em.close();
//	        }
//	    }
//	    return res;
//	}

	

	public List<Ride> getRidesByDriver(String username) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<Driver> query = em.createQuery("SELECT d FROM Driver d WHERE d.username = :username",
					Driver.class);
			query.setParameter("username", username);
			Driver driver = query.getSingleResult();

			List<Ride> rides = driver.getCreatedRides();

			em.getTransaction().commit();
			return rides;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		}
	}

	public String getDriverNameByEmail(String email) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<String> query = em.createQuery("SELECT d.name FROM Driver d WHERE d.email = :email",
					String.class);
			query.setParameter("email", email);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}
}
