package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource extends GenericResource {

    @Inject
    FlightRepository flightRepository;

    @Inject
    Validator validator;

    @GET
    public Response getFlights() {
        var list = flightRepository.listAll();
        return getOr404(list);
    }

    @GET
    public Response getFlightsByDestination(@QueryParam("destination") String destination) {

        List<Flight> flights;

        if (StringUtils.isNotBlank(destination))
            flights = flightRepository.findByDestination(destination);
        else
            flights = flightRepository.listAll();

        return getOr404(flights);
    }

    @POST
    @Transactional
    public Response createFlight(Flight flight) {
        var violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }
        try {
            flightRepository.persist(flight);
            return Response.ok(flight).status(201).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getFlightById(@PathParam("id") Long id) {
        var flight = flightRepository.findByIdOptional(id);
        return getOr404(flight);
    }

    @DELETE
    @Transactional
    public Response deleteFlight(@QueryParam("id") Long id) {
        var flight = flightRepository.findByIdOptional(id);
        if (flight.isPresent()) {
            flightRepository.delete(flight.get());
            return Response.ok().status(204).build();
        } else {
            return Response.noContent().status(404).build();
        }
    }

}



