package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.h2.table.Plan;

import java.util.List;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource extends GenericResource {

    @Inject
    FlightRepository flightRepository;

    @Inject
    PlaneRepository planeRepository;

    @Inject
    Validator validator;

    @GET
    public Response getFlights(@QueryParam("destination") String destination) {

        List<Flight> flights;

        if (StringUtils.isNotBlank(destination))
            flights = flightRepository.findByDestination(destination);
        else
            flights = flightRepository.listAll();

        return getOr404(flights);
    }

    @GET
    @Path("/{id}")
    public Response getFlightById(@PathParam("id") Long id) {
        var flight = flightRepository.findByIdOptional(id);
        return getOr404(flight);
    }

    @POST
    @Transactional
    public Response createFlight(Flight flight) {

        var violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        Plane plane = planeRepository.getById(flight.getPlane().getId());
        if (plane == null) {
            return Response.status(400).entity(new ErrorWrapper("The plane does not exist")).build();
        }

        try {
            flightRepository.persist(flight);
            return Response.ok(flight).status(201).entity("Flight created").build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorWrapper("Error during flight registration")).status(500).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteFlight(@PathParam("id") Long id) {

        Flight flight = flightRepository.getById(id);
        if (flight == null) {
            return Response.status(400).entity(new ErrorWrapper("The flight does not exist")).build();
        }

        try {
            flightRepository.delete(flight);
            return Response.ok().status(201).entity("The flight has been deleted").build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

}



