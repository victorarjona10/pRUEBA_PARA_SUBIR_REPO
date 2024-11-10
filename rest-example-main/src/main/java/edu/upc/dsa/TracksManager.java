package edu.upc.dsa;

import edu.upc.dsa.exceptions.TrackNotFoundException;
import edu.upc.dsa.models.Track;
import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.FlightPlanReservation;


import java.util.Date;
import java.util.List;

public interface TracksManager {


    public Track addTrack(String id, String title, String singer);
    public Track addTrack(String title, String singer);
    public Track addTrack(Track t);
    public Track getTrack(String id);
    public Track getTrack2(String id) throws TrackNotFoundException;

    public List<Track> findAll();
    public void deleteTrack(String id);
    public Track updateTrack(Track t);

    public void clear();
    public int size();

    public Dron addDron(String id, String name, String manufacturer, String model);
    public Piloto addPiloto(String id, String nombre, String apellidos);
    public List<Dron> getDronesOrderedByFlightHours();
    public List<Piloto> getPilotosOrderedByFlightHours();
    public void storeDronForMaintenance(String dronId);
    public Dron performMaintenanceOnDron();
    public String addFlightPlanReservation(String dronId, Date date, int estimatedTime, String startPosition, String endPosition, String pilotoId);
    public List<FlightPlanReservation> getFlightPlanReservationsForPiloto(String pilotoId);
    public List<FlightPlanReservation> getFlightPlanReservationsForDron(String dronId);





}
