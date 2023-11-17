package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.repositories.PlaneRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/planes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlaneResources extends GenericResource {

    @Inject
    PlaneRepository planeRepository;

    @GET
    public Response getPlanes() {
        var list = planeRepository.listAll();
        return getOr404(list);
    }

    @GET
    @Path("/{id}")
    public Response getPlaneById(@PathParam("id") Long id) {
        var plane = planeRepository.findByIdOptional(id);
        return getOr404(plane);
    }
}