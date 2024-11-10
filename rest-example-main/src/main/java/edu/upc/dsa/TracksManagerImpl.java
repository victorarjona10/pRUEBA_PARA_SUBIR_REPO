package edu.upc.dsa;

import edu.upc.dsa.exceptions.TrackNotFoundException;
import edu.upc.dsa.models.Track;
import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.FlightPlanReservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class TracksManagerImpl implements TracksManager {
    private static TracksManager instance;
    protected List<Track> tracks;
    protected List<Dron> drones;
    protected List<Piloto> pilots;

    LinkedList<Dron> maintenanceQueue = new LinkedList<>();

    protected List<FlightPlanReservation> flightPlanReservations;
    final static Logger logger = Logger.getLogger(TracksManagerImpl.class);

    private TracksManagerImpl() {

        this.tracks = new LinkedList<>();
        this.drones = new LinkedList<>();
        this.pilots = new LinkedList<>();
        this.flightPlanReservations = new LinkedList<>();
        this.maintenanceQueue = new LinkedList<>();
    }

    public static TracksManager getInstance() {
        if (instance==null) instance = new TracksManagerImpl();
        return instance;
    }


    @Override
    public Dron addDron(String id, String name, String manufacturer, String model) {
        logger.info("addDron called with parameters: id=" + id + ", name=" + name + ", manufacturer=" + manufacturer + ", model=" + model);

        Dron newDron = new Dron(id, name, manufacturer, model);
        this.drones.add(newDron);

        logger.info("addDron finished, new dron added: " + newDron.getName());
        return newDron;
    }

    @Override
    public Piloto addPiloto(String id, String nombre, String apellidos) {
        logger.info("addPiloto called with parameters: id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos);

        Piloto newPiloto = new Piloto(id, nombre, apellidos);
        this.pilots.add(newPiloto);

        logger.info("addPiloto finished, new pilot added: " + newPiloto.getNombre());
        return newPiloto;
    }

    @Override
    public List<Dron> getDronesOrderedByFlightHours() {
        logger.info("getDronesOrderedByFlightHours called");

        List<Dron> sortedDrones = new ArrayList<>(this.drones);
        sortedDrones.sort((d1, d2) -> d2.getFlightHours() - d1.getFlightHours());
        return sortedDrones;
    }

    @Override
    public List<Piloto> getPilotosOrderedByFlightHours() {
        logger.info("getPilotosOrderedByFlightHours called");

        List<Piloto> sortedPilots = new ArrayList<>(this.pilots);
        sortedPilots.sort((p1, p2) -> p2.getFlightHours() - p1.getFlightHours());
        return sortedPilots;
    }

    @Override
    public void storeDronForMaintenance(String dronId) {
        logger.info("storeDronForMaintenance called with parameter: dronId=" + dronId);

        for (Dron d: this.drones) {
            if (d.getId().equals(dronId)) {
                logger.info("getTrack("+dronId+"): "+d);

                this.maintenanceQueue.add(d);
                logger.info("storeDronForMaintenance finished, dron added to maintenance queue: " + d.getName());
                d.setInMaintenance(true);
            }
        }

    }


    @Override
    public Dron performMaintenanceOnDron() {
        logger.info("performMaintenanceOnDron called");

        Dron d = this.maintenanceQueue.removeLast();
        d.setInMaintenance(false);

        logger.info("performMaintenanceOnDron finished, dron: " + d.getName() + " is no longer in maintenance");
        return d;
    }

    @Override
    public String addFlightPlanReservation(String dronId, Date date, int estimatedTime, String startPosition, String endPosition, String pilotoId) {
        logger.info("addFlightPlanReservation called with parameters: dronId=" + dronId + ", date=" + date + ", estimatedTime=" + estimatedTime + ", startPosition=" + startPosition + ", endPosition=" + endPosition + ", pilotoId=" + pilotoId);

        // Check if the dron is in maintenance
        for (Dron d : this.drones) {
            if (d.getId().equals(dronId) && d.isInMaintenance()) {
                logger.error("Dron " + dronId + " is in maintenance and cannot be assigned a flight plan.");

                return "Dron is in maintenance and cannot be assigned a flight plan.";
            }
        }

        // Check for overlapping flight plans for the dron
        for (FlightPlanReservation fpr : this.flightPlanReservations) {
            if (fpr.getDronId().equals(dronId) && isOverlapping(fpr, date, estimatedTime)) {
                logger.error("Dron " + dronId + " already has a flight plan in the given time interval.");

                return "Dron already has a flight plan in the given time interval.";
            }
        }

        // Check for overlapping flight plans for the piloto
        for (FlightPlanReservation fpr : this.flightPlanReservations) {
            if (fpr.getPilotoId().equals(pilotoId) && isOverlapping(fpr, date, estimatedTime)) {
                logger.error("Piloto " + pilotoId + " already has a flight plan in the given time interval.");

                return "Piloto already has a flight plan in the given time interval.";
            }
        }

        FlightPlanReservation newFlightPlanReservation = new FlightPlanReservation(dronId, date, estimatedTime, startPosition, endPosition, pilotoId);
        this.flightPlanReservations.add(newFlightPlanReservation);

        logger.info("addFlightPlanReservation finished, new flight plan reservation added: " + newFlightPlanReservation.getDronId());
        return "New flight plan reservation added.";
    }

    private boolean isOverlapping(FlightPlanReservation fpr, Date date, int estimatedTime) {
        long startTime = date.getTime();
        long endTime = startTime + estimatedTime * 3600000L; // Convert hours to milliseconds
        long fprStartTime = fpr.getDate().getTime();
        long fprEndTime = fprStartTime + fpr.getEstimatedTime() * 3600000L;

        return (startTime < fprEndTime && endTime > fprStartTime);
    }


    @Override
    public List<FlightPlanReservation> getFlightPlanReservationsForPiloto(String pilotoId) {
        logger.info("getFlightPlanReservationsForPiloto called with parameter: pilotoId=" + pilotoId);

        List<FlightPlanReservation> flightPlanReservationsForPiloto = new ArrayList<>();

        for (FlightPlanReservation fpr : this.flightPlanReservations) {
            if (fpr.getPilotoId().equals(pilotoId)) {
                flightPlanReservationsForPiloto.add(fpr);
            }
        }

        logger.info("getFlightPlanReservationsForPiloto finished, returning list of flight plan reservations for pilot with id: " + pilotoId);
        return flightPlanReservationsForPiloto;
    }

    @Override
    public List<FlightPlanReservation> getFlightPlanReservationsForDron(String dronId) {
        logger.info("getFlightPlanReservationsForDron called with parameter: dronId=" + dronId);

        List<FlightPlanReservation> flightPlanReservationsForDron = new ArrayList<>();

        for (FlightPlanReservation fpr : this.flightPlanReservations) {
            if (fpr.getDronId().equals(dronId)) {
                flightPlanReservationsForDron.add(fpr);
            }
        }

        logger.info("getFlightPlanReservationsForDron finished, returning list of flight plan reservations for dron with id: " + dronId);
        return flightPlanReservationsForDron;
    }




  //------------------------------------------------------------------------------------------------------------------------------------------------------------
    public int size() {
        int ret = this.drones.size() + this.pilots.size() + this.flightPlanReservations.size();
        logger.info("size " + ret);

        return ret;
    }

    public Track addTrack(Track t) {
        logger.info("new Track " + t);

        this.tracks.add (t);
        logger.info("new Track added");
        return t;
    }

    public Track addTrack(String title, String singer){
        return this.addTrack(null, title, singer);
    }

    public Track addTrack(String id, String title, String singer) {
        return this.addTrack(new Track(id, title, singer));
    }

    public Track getTrack(String id) {
        logger.info("getTrack("+id+")");

        for (Track t: this.tracks) {
            if (t.getId().equals(id)) {
                logger.info("getTrack("+id+"): "+t);

                return t;
            }
        }

        logger.warn("not found " + id);
        return null;
    }

    public Track getTrack2(String id) throws TrackNotFoundException {
        Track t = getTrack(id);
        if (t == null) throw new TrackNotFoundException();
        return t;
    }


    public List<Track> findAll() {
        return this.tracks;
    }

    @Override
    public void deleteTrack(String id) {

        Track t = this.getTrack(id);
        if (t==null) {
            logger.warn("not found " + t);
        }
        else logger.info(t+" deleted ");

        this.tracks.remove(t);

    }

    @Override
    public Track updateTrack(Track p) {
        Track t = this.getTrack(p.getId());

        if (t!=null) {
            logger.info(p+" rebut!!!! ");

            t.setSinger(p.getSinger());
            t.setTitle(p.getTitle());

            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+p);
        }

        return t;
    }

    public void clear() {
        this.tracks.clear();
    }
}