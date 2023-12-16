package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.repositories.PassengerRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

        // Cette requête permet de récupérer tous les passagers si aucun paramètre n'est renseigné
        // Sinon, on récupère les passagers correspondants aux paramètres renseignés : nom, prénom ou email

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
    public Response updatePassenger(@Valid Passenger passenger) {

        // Vérifie que le passager existe et que les données sont valides (contraintes de l'entité respectées)
        // La présence de @Valid permet de forcer le passage par le validator pour forcer la validation de l'Email (contrainte @Email)
        var violations = validator.validate(passenger);

        // Si des violations sont détectées, on renvoie une erreur 400 avec les violations
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        // Récupère le passager à modifier
        Passenger passengerToUpdate = passengerRepository.findById(passenger.getId());

        // Si le passager n'existe pas, on renvoie une erreur 404
        if (passengerToUpdate == null) {
            return Response.status(404).entity(new ErrorWrapper("Passenger not found")).build();
        }

        // Si le passager existe, on le met à jour en utilisant la méthode update du repository
        try {
            passengerRepository.update(passengerToUpdate, passenger);
            return Response.ok().status(201).entity("Passenger updated").build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during passenger update")).status(500).build();
        }
    }
}