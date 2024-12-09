package modelo.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Driver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlID
	@Id 
	private String email;
	private String name; 
	private String password;
	
    //@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //private Login  login;

	@XmlIDREF
	
	@OneToMany(mappedBy = "driver", targetEntity = Ride.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Ride> createdRides = new ArrayList<Ride>();

	public Driver() {
		super();
	}

	public Driver(String email, String name, String password){
		this.password = password;
		this.email = email;
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public String toString(){
		return email+";"+name+ createdRides;
	}
	
	public List<Ride> getCreatedRides() {
		return createdRides;
	}

	public void setCreatedRides(List<Ride> createdRides) {
		this.createdRides = createdRides;
	}
	
	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(from,to,date,nPlaces,price, this);
        createdRides.add(ride);
        return ride;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
//	public boolean doesRideExists(String from, String to, Date date)  {	
//		for (Ride r:createdRides)
//			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
//			 return true;
//		
//		return false;
//	}
	public boolean doesRideExist(EntityManager em, String driverEmail, String from, String to, Date date) {
	    TypedQuery<Long> query = em.createQuery(
	        "SELECT COUNT(r) FROM Ride r WHERE r.driver.email = :driverEmail AND r.from = :from AND r.to = :to AND r.date = :date", 
	        Long.class
	    );
	    query.setParameter("driverEmail", driverEmail);
	    query.setParameter("from", from);
	    query.setParameter("to", to);
	    query.setParameter("date", date);

	    return query.getSingleResult() > 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (email != other.email)
			return false;
		return true;
	}

	public Ride removeRide(String from, String to, Date date) {
		boolean found=false;
		int index=0;
		Ride r=null;
		while (!found && index<=createdRides.size()) {
			r=createdRides.get(++index);
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			found=true;
		}
			
		if (found) {
			createdRides.remove(index);
			return r;
		} else return null;
	}


	
}
