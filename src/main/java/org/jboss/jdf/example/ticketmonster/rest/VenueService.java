package org.jboss.jdf.example.ticketmonster.rest;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import org.jboss.jdf.example.ticketmonster.model.Venue;

@Path("/venues")
@ApplicationScoped
@Singleton
public class VenueService extends BaseEntityService<Venue> {

    public VenueService() {
        super(Venue.class);
    }


}