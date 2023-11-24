package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import fr.unilasalle.flight.api.repositories.PassengerRepository;
import fr.unilasalle.flight.api.repositories.ReservationRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
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
    PassengerRepository passengerRepository;

    @Inject
    FlightRepository flightRepository;

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
    @Transactional
    public Response createReservation(Reservation reservation) {
        var violations = validator.validate(reservation);

        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        if (reservationRepository.countByFlightNumber(reservation.getFlight().getNumber()) >= reservation.getFlight().getPlane().getCapacity()){
            return Response.status(400).entity(new ErrorWrapper("The plane is full")).build();
        }

        Flight flight;
        if (StringUtils.isNotBlank(reservation.getFlight().getNumber())) {
            flight = flightRepository.findById(reservation.getFlight().getId());
            if (flight == null) {
                return Response.status(400).entity(new ErrorWrapper("Provided Flight does not exist")).build();
            }
            reservation.setFlight(flight);
        }

        Passenger passenger;
        List<Passenger> existingPassengers = passengerRepository.findByEmail(reservation.getPassenger().getEmail());
        if (existingPassengers.isEmpty()) {
            passenger = reservation.getPassenger();
            passenger.setId(null);
            passengerRepository.persist(passenger);
        } else {
            passenger = existingPassengers.get(0); // Utilisez le passager existant
        }

        reservation.setPassenger(passenger);
        reservationRepository.persist(reservation);

        try {
            reservationRepository.persistAndFlush(reservation);
            return Response.ok(reservation).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
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