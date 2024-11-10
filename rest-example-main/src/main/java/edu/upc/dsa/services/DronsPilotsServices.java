package edu.upc.dsa.services;


import edu.upc.dsa.TracksManager;
import edu.upc.dsa.TracksManagerImpl;
import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.FlightPlanReservation;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.Track;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;


@Api(value = "/drons&pilots", description = "Endpoint to Track Service")
@Path("/drons&pilots")
public class DronsPilotsServices {

    final static Logger logger = Logger.getLogger(DronsPilotsServices.class);

    private TracksManager tm;

    public DronsPilotsServices() {
        this.tm = TracksManagerImpl.getInstance();
        if (tm.size() == 0) {


            Dron Dron1 = this.tm.addDron("1", "Dron1", "DronManufacturer1", "DronModel1");
            Dron1.setFlightHours(100);

            Dron Dron2 = this.tm.addDron("2", "Dron2", "DronManufacturer2", "DronModel2");
            Dron2.setFlightHours(500);

            Dron Dron3 = this.tm.addDron("3", "Dron3", "DronManufacturer3", "DronModel3");
            Dron3.setFlightHours(400);

            Piloto Piloto1 = this.tm.addPiloto("1", "Piloto1", "PilotoSurname1");
            Piloto1.setFlightHours(100);

            Piloto Piloto2 = this.tm.addPiloto("2", "Piloto2", "PilotoSurname2");
            Piloto2.setFlightHours(500);

            Piloto Piloto3 = this.tm.addPiloto("3", "Piloto3", "PilotoSurname3");
            Piloto3.setFlightHours(200);
            tm.size();
        }
    }

    @GET
    @ApiOperation(value = "get all Drones", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Dron.class, responseContainer = "List"),
    })
    @Path("/drones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrones() {

        List<Dron> drones = this.tm.getDronesOrderedByFlightHours();

        logger.info("drones: " + drones);
        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(drones) {
        };
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all Pilots", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Piloto.class, responseContainer = "List"),
    })
    @Path("/pilots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPilots() {

        List<Piloto> pilotos = this.tm.getPilotosOrderedByFlightHours();


        GenericEntity<List<Piloto>> entity = new GenericEntity<List<Piloto>>(pilotos) {
        };
        return Response.status(201).entity(entity).build();
    }


    @POST
    @ApiOperation(value = "create a new Dron", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Dron.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/drones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newDron(Dron dron) {

        if (dron.getId()==null || dron.getName()==null || dron.getModel()== null || dron.getManufacturer()== null)
            return Response.status(500).entity(dron).build();

        this.tm.addDron(dron.getId(), dron.getName(), dron.getManufacturer(), dron.getModel());
        return Response.status(201).entity(dron).build();
    }

    @POST
    @ApiOperation(value = "create a new Piloto", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Piloto.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/pilotos")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newDron(Piloto piloto) {

            if (piloto.getId()==null || piloto.getNombre()==null || piloto.getApellidos()== null)
            return Response.status(500).entity(piloto).build();

        this.tm.addPiloto(piloto.getId(), piloto.getNombre(), piloto.getApellidos());
        return Response.status(201).entity(piloto).build();
    }
    @POST
    @ApiOperation(value = "guardar dron para mantenimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/drones/mantenimiento/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response storeDronForMaintenance(@PathParam("id") String id) {
        this.tm.storeDronForMaintenance(id);

        return Response.status(200).build();
    }

    @POST
    @ApiOperation(value = "reparar el ultimo dron que ha entrado en mantenimiento", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/drones/reparar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response repairLastDronForMaintenance() {
        this.tm.performMaintenanceOnDron();

        return Response.status(200).build();
    }

    //AHORA QUIERO AÑADIR UNA RESERVA PARA UN VUELO,USANDO LA FUNCION addFlightPlanReservation
    @POST
    @ApiOperation(value = "añadir una reserva de vuelo", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=String.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/reservas/{DronId}/{PilotoId}/{Date_yyyy-MM-dd}/{Origin}/{Destination}/{Duration}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFlightPlanReservation(@PathParam("DronId") String DronId, @PathParam("PilotoId") String PilotoId, @PathParam("Date_yyyy-MM-dd") String Date, @PathParam("Origin") String Origin, @PathParam("Destination") String Destination, @PathParam("Duration") int Duration) {




        if (DronId==null || PilotoId==null  || Origin== null || Destination== null || Duration==0)
            return Response.status(500).build();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = formatter.parse(Date);
        } catch (ParseException e) {
            date = new Date();
        }


        logger.info("valor dronid: " +DronId + " valor pilotoId: " + PilotoId + " valor date: " + date + " valor origin: " + Origin + " valor destination: " + Destination + " valor duration: " + Duration);

        String respuesta = this.tm.addFlightPlanReservation(DronId, date, Duration, Origin, Destination, PilotoId);

        return Response.status(200).entity(respuesta).build();
    }
    @POST
    @ApiOperation(value = "Lista de reservas de vuelo que han sido asignadas a un piloto", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=FlightPlanReservation.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/reservas/Piloto/{PilotoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFlightPlanReservationsForPiloto(@PathParam("PilotoId") String PilotoId) {
        List<FlightPlanReservation> reservations = this.tm.getFlightPlanReservationsForPiloto(PilotoId);

        GenericEntity<List<FlightPlanReservation>> entity = new GenericEntity<List<FlightPlanReservation>>(reservations) {
        };
        return Response.status(201).entity(entity).build();
    }
    @POST
    @ApiOperation(value = "Lista de reservas de vuelo que han sido asignadas a un dron", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=FlightPlanReservation.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/reservas/Dron/{DronId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFlightPlanReservationsForDron(@PathParam("DronId") String DronId) {
        List<FlightPlanReservation> reservations = this.tm.getFlightPlanReservationsForDron(DronId);

        GenericEntity<List<FlightPlanReservation>> entity = new GenericEntity<List<FlightPlanReservation>>(reservations) {
        };
        return Response.status(201).entity(entity).build();
    }



}
