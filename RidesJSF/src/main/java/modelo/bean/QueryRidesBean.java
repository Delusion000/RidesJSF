package modelo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import modelo.dominio.Ride;
import principal.HibernateDataAccess;

@Named("queryRidesBean")
@ViewScoped
public class QueryRidesBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String selectedDepartCity;
    private String selectedArrivalCity;
    private Date selectedDate;
    private List<String> departCities;
    private List<String> arrivalCities;
    private List<Ride> matchingRides = new ArrayList<>();

    private HibernateDataAccess dataAccess = new HibernateDataAccess();

    public QueryRidesBean(){
    	
    	
    }
    public void loadArrivalCities() {
        if (selectedDepartCity != null) {
            arrivalCities = dataAccess.getArrivalCities(selectedDepartCity);
            System.out.println("Ciudades de llegada para " + selectedDepartCity + ": " + arrivalCities); // DEBUG
        } else {
            arrivalCities = null;
            System.out.println("selectedDepartCity es nulo o vacío."); // DEBUG
        }
    }

    public void searchRides() {
    	 System.out.print("search ejecutado");
        if (selectedDepartCity == null || selectedDepartCity.isEmpty() ||
        		selectedArrivalCity == null || selectedArrivalCity.isEmpty() || selectedDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos las variables deben estar seleccionadas", null ));
            System.out.print("Problema encontrado en search");
            return;
        }
        
        matchingRides = dataAccess.getRides(selectedDepartCity, selectedArrivalCity, selectedDate);
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Viajes encontrado", null ));
        System.out.print("rearch ridess" + matchingRides);
        
    }

    // Getters and Setters
    public String getSelectedDepartCity() {
        return selectedDepartCity;
    }

    public void setSelectedDepartCity(String selectedDepartCity) {
        this.selectedDepartCity = selectedDepartCity;
    }

    public String getSelectedArrivalCity() {
        return selectedArrivalCity;
    }

    public void setSelectedArrivalCity(String selectedArrivalCity) {
        this.selectedArrivalCity = selectedArrivalCity;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<String> getDepartCities() {
    	departCities = dataAccess.getDepartCities();
        System.out.println("Ciudades de partida inicializadas en el bean: " + departCities); // DEBUG
        return departCities;
    }

    public List<String> getArrivalCities() {
        return arrivalCities;
    }

    public List<Ride> getMatchingRides() {
        return matchingRides;
    }
}
