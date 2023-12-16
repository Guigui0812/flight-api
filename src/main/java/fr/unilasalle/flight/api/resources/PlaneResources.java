package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Path("/planes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlaneResources extends GenericResource {

    @Inject
    PlaneRepository planeRepository;

    @Inject
    Validator validator;

    @GET
    public Response getPlanesByOperator(@QueryParam("operator") String operator) {

        List<Plane> planes;

        // Si l'opérateur est renseigné en QueryParam, on récupère les avions correspondants
        // Sinon, on récupère tous les avions
        if (StringUtils.isNotBlank(operator))
            planes = planeRepository.findByOperator(operator);
        else
            planes = planeRepository.listAll();

        return getOr404(planes);
    }

    @GET
    @Path("/{id}")
    public Response getPlaneById(@PathParam("id") Long id) {
        // Récupère l'avion par son id si spécifié en PathParam
        var plane = planeRepository.findByIdOptional(id);
        return getOr404(plane);
    }

    @POST
    @Transactional
    public Response createPlane(Plane plane) {
        var violations = validator.validate(plane);

        // Si des violations sont détectées, on renvoie une erreur 400 avec les violations
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }
        try {
            // Sinon, on persiste l'avion en base de données
            planeRepository.persistAndFlush(plane);
            return Response.ok().status(201).entity("Plane created").build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during plane registration")).status(500).build();
        }
    }
}