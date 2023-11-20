package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
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

    // Pas possible de mettre deux fois le GET car pour lui c'est la même, du coup faut modifier l'URL du path

    @GET
    public Response getPlanes() {
        var list = planeRepository.listAll();
        return getOr404(list);
    }

    @GET
    public Response getPlanesByOperator(@QueryParam("operator") String operator) {

        List<Plane> planes;

        if (StringUtils.isNotBlank(operator))
            planes = planeRepository.findByOperator(operator);
        else
            planes = planeRepository.listAll();

        return getOr404(planes);
    }

    @GET
    @Path("/{id}")
    public Response getPlaneById(@PathParam("id") Long id) {
        var plane = planeRepository.findByIdOptional(id);
        return getOr404(plane);
    }

    @POST
    public Response createPlane(Plane plane) {
        try {
            planeRepository.persistAndFlush(plane);
            return Response.ok().status(201).build();
        } catch (PersistenceException e) {
            return Response.serverError().entity(new ErrorWrapper("Error during plane registration")).status(500).build();
        }
    }
}