package org.jboss.jdf.example.ticketmonster.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.model.MediaItem;

@Path("/media")
@RequestScoped
@Stateful
public class MediaService {

    @Inject
    private Logger log;
    @Inject
    private EntityManager em;
    
    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("*/*")
    public Response getMediaContent(@PathParam("id") Long id) {
        Response.ResponseBuilder builder = null;
        try {
            MediaItem item = em.find(MediaItem.class, id);
            if (item != null ) {
                builder = Response.ok();
                builder.type(MediaType.valueOf(item.getMediaType()));
                builder.entity(item.getContent());
            } else {
                Map<String, String> response = new HashMap<String, String>();
                response.put("id", "No media item with id '" + id  + "' exists");
                builder = Response.status(Response.Status.NOT_FOUND).entity(response);
            }
        } catch (Exception e) {
           log.log(Level.SEVERE, "Error while retrieving media content: ", e);
           builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }
        return builder.build();
    }
}
