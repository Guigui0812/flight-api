package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
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

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource extends GenericResource {

    @Inject
    FlightRepository flightRepository;

    @Inject
    PlaneRepository planeRepository;

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    Validator validator;

    @GET
    public Response getFlights(@QueryParam("destination") String destination) {

        List<Flight> flights;

        // Si la destination est renseignée en QueryParam, on récupère les vols correspondants
        // Sinon, on récupère tous les vols
        if (StringUtils.isNotBlank(destination))
            flights = flightRepository.findByDestination(destination);
        else
            flights = flightRepository.listAll();

        return getOr404(flights);
    }

    @GET
    @Path("/{id}")
    public Response getFlightById(@PathParam("id") Long id) {
        // Récupère le vol par son id si spécifié en PathParam
        var flight = flightRepository.findByIdOptional(id);
        return getOr404(flight);
    }

    @POST
    @Transactional
    public Response createFlight(Flight flight) {

        // Vérifie que le vol est valide (contraintes de l'entité respectées)
        var violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        // Vérifie que l'avion utilisé pour le vol existe
        Plane plane = planeRepository.getById(flight.getPlane().getId());
        if (plane == null) {
            return Response.status(404).entity(new ErrorWrapper("The plane does not exist")).build();
        }

        // Création du vol
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

        // Récupère le vol et vérifie qu'il existe, sinon retourne une erreur 404
        Flight flight = flightRepository.getById(id);
        if (flight == null) {
            return Response.status(404).entity(new ErrorWrapper("The flight does not exist")).build();
        }

        try {

            // Récupère les réservations associées au vol et les supprime
            List<Reservation> reservations = reservationRepository.findByFlightId(id);

            // Boucle sur les réservations et les supprime
            for (Reservation reservation : reservations) {
                reservationRepository.delete(reservation);
            }

            // Supprime le vol une fois que toutes les réservations ont été supprimées
            flightRepository.delete(flight);
            return Response.ok().status(201).entity("The flight has been deleted with all its reservations").build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorWrapper("Error during flight deletion")).status(500).build();
        }
    }
}