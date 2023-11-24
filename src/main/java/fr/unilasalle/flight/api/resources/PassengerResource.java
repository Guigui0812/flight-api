package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.repositories.PassengerRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerResource extends GenericResource {

    @Inject
    PassengerRepository passengerRepository;

    @Inject
    Validator validator;

    @GET
    public Response getPassengers(@QueryParam("surname") String surname, @QueryParam("firstname") String firstName, @QueryParam("email") String email) {

        List<Passenger> passengers;

        if ((StringUtils.isEmpty(surname) || StringUtils.isEmpty(firstName)) && StringUtils.isEmpty(email)) {
            passengers = passengerRepository.listAll();
        }
        else if (StringUtils.isNotEmpty(email) && (StringUtils.isEmpty(surname) && StringUtils.isEmpty(firstName))) {
            passengers = passengerRepository.findByEmail(email);
        }
        else {
            passengers = passengerRepository.findBySurnameAndFirstName(surname, firstName);
        }

        return getOr404(passengers);
    }

    @PUT
    @Transactional
    public Response updatePassenger(Passenger passenger) {
        var violations = validator.validate(passenger);

        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        Passenger passengerToUpdate = passengerRepository.findById(passenger.getId());

        if (passengerToUpdate == null) {
            return Response.status(404).entity(new ErrorWrapper("Passenger not found")).build();
        }

        try {
            passengerRepository.update(passengerToUpdate, passenger);
            return Response.ok().status(201).entity("Passenger updated").build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during passenger update")).status(500).build();
        }
    }
}