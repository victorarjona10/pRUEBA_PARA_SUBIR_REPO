package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.List;

public class Dron {
    private String id;
    private String name;
    private String manufacturer;
    private String model;
    private int flightHours;
    private boolean isInMaintenance;
    private List<FlightPlanReservation> flightPlanReservations;

    public Dron(String id, String name, String manufacturer, String model) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.flightHours = 0;
        this.isInMaintenance = false;
        this.flightPlanReservations = new ArrayList<>();
    }

    public Dron() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(int flightHours) {
        this.flightHours = flightHours;
    }

    public boolean isInMaintenance() {
        return isInMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        isInMaintenance = inMaintenance;
    }

    public List<FlightPlanReservation> getFlightPlanReservations() {
        return flightPlanReservations;
    }

    public void setFlightPlanReservations(List<FlightPlanReservation> flightPlanReservations) {
        this.flightPlanReservations = flightPlanReservations;
    }

    public void addFlightPlanReservation(FlightPlanReservation flightPlanReservation) {
        this.flightPlanReservations.add(flightPlanReservation);
    }

    public void removeFlightPlanReservation(FlightPlanReservation flightPlanReservation) {
        this.flightPlanReservations.remove(flightPlanReservation);
    }

    @Override
    public String toString() {
        return "Dron [id=" + id + ", name=" + name + ", manufacturer=" + manufacturer + ", model=" + model + ", flightHours=" + flightHours + ", isInMaintenance=" + isInMaintenance + ", flightPlanReservations=" + flightPlanReservations + "]";
    }
}
