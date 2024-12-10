//package modelo.bean;
//
//import principal.HibernateDataAccess;
//import modelo.dominio.Ride;
//
//import javax.annotation.PostConstruct;
//
//import jakarta.annotation.ManagedBean;
//import jakarta.enterprise.context.RequestScoped;
//import jakarta.inject.Named;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Named("rideBean")
//@RequestScoped
//public class RideBean {
//
//    private HibernateDataAccess rideDAO = new HibernateDataAccess();
//
//    private String selectedFrom;
//    private String selectedTo;
//    private Date selectedDate;
//    private List<Ride> availableRides;
//
//    private List<String> fromLocations = new ArrayList<>();
//    private List<String> toLocations;
//
//    @PostConstruct
//    public void init() {
//        // Cargar ubicaciones únicas desde la base de datos
//        List<Ride> rides = rideDAO.findAll();
//        fromLocations = rides.stream().map(Ride::getFrom).distinct().toList();
//        toLocations = rides.stream().map(Ride::getTo).distinct().toList();
//    }
//    
//    public List<String> getFromLocation(){
//    	
//		return fromLocations;
//    	
//    }
//
//    public void searchRides() {
//        availableRides = rideDAO.findRides(selectedFrom, selectedTo, selectedDate);
//    }
//
//    // Getters y setters
//    public String getSelectedFrom() {
//        return selectedFrom;
//    }
//
//    public void setSelectedFrom(String selectedFrom) {
//        this.selectedFrom = selectedFrom;
//    }
//
//    public String getSelectedTo() {
//        return selectedTo;
//    }
//
//    public void setSelectedTo(String selectedTo) {
//        this.selectedTo = selectedTo;
//    }
//
//    public Date getSelectedDate() {
//        return selectedDate;
//    }
//
//    public void setSelectedDate(Date selectedDate) {
//        this.selectedDate = selectedDate;
//    }
//
//    public List<Ride> getAvailableRides() {
//        return availableRides;
//    }
//
//    public List<String> getFromLocations() {
//        return fromLocations;
//    }
//
//    public List<String> getToLocations() {
//        return toLocations;
//    }
//}
//
