package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
import fr.unilasalle.flight.api.repositories.ReservationRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource extends GenericResource {

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    Validator validator;

    @GET
    public Response getReservationsByFlight(@QueryParam("flightNumber") String flightNumber) {

        List<Reservation> reservations;

        if (StringUtils.isNotBlank(flightNumber))
            reservations = reservationRepository.findByFlightNumber(flightNumber);
        else
            reservations = reservationRepository.listAll();

        return getOr404(reservations);
    }

    // Penser à baisser la capacité de l'avion si une place est réservée
    @POST
    public Response createReservation(Reservation reservation) {
        var violations = validator.validate(reservation);

        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }
        try {
            reservationRepository.persistAndFlush(reservation);
            return Response.ok().status(201).build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during reservation registration")).status(500).build();
        }
    }

    // Penser à mettre à jour la capacité de l'avion si une place se libère
    @DELETE
    @Path("/{id}")
    public Response deleteReservation(@PathParam("id") Long id) {
        var reservation = reservationRepository.findByIdOptional(id);
        if (reservation.isEmpty()) {
            return Response.noContent().status(404).build();
        }
        try {
            reservationRepository.delete(reservation.get());
            return Response.ok().status(204).entity("Reservation deleted").build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during reservation deletion")).status(500).build();
        }
    }
}
