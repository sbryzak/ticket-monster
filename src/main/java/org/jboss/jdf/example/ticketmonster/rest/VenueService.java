package org.jboss.jdf.example.ticketmonster.rest;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

import org.jboss.jdf.example.ticketmonster.model.Venue;

@Path("/venues")
@ApplicationScoped
@Singleton
public class VenueService extends BaseEntityService<Venue> {

    public VenueService() {
        super(Venue.class);
    }

}