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

        // Si le numéro de vol est renseigné en QueryParam, on récupère les réservations correspondantes
        // Sinon, on récupère toutes les réservations
        if (StringUtils.isNotBlank(flightNumber))
            reservations = reservationRepository.findByFlightNumber(flightNumber);
        else
            reservations = reservationRepository.listAll();

        return getOr404(reservations);
    }

    @POST
    @Transactional
    public Response createReservation(Reservation reservation) {
        var violations = validator.validate(reservation);

        // Si des violations sont détectées, on renvoie une erreur 400 avec les violations
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        // Vérifie que le vol existe
        Flight flight = flightRepository.findById(reservation.getFlight().getId());

        if (flight == null) {
            return Response.status(404).entity(new ErrorWrapper("Provided Flight does not exist")).build();
        }

        reservation.setFlight(flight);

        // Vérifie que le nombre de réservations n'a pas atteint la capacité de l'avion
        if (reservationRepository.countByFlightNumber(flight.getNumber()) >= flight.getPlane().getCapacity()) {
            return Response.status(400).entity(new ErrorWrapper("The plane is full")).build();
        }

        // Vérifie que le passager existe, sinon le crée
        Passenger passenger;
        List<Passenger> existingPassengers = passengerRepository.findByEmail(reservation.getPassenger().getEmail());
        if (existingPassengers.isEmpty()) {
            passenger = reservation.getPassenger();
            passenger.setId(null); // Forcer l'ID à null afin de le générer automatiquement
            passengerRepository.persist(passenger); // Création du passager en base de données
        } else {
            passenger = existingPassengers.get(0); // Utilise le passager existant
        }

        // Ajoute le passager à la réservation avec le bon ID
        reservation.setPassenger(passenger);

        // Enregistre la réservation en base de données
        reservationRepository.persist(reservation);

        try {
            reservationRepository.persistAndFlush(reservation);
            return Response.ok(reservation).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }

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