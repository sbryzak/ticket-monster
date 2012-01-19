package org.jboss.jdf.example.ticketmonster.rest;

import java.util.List;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.Venue;

@Path("/venues")
@ApplicationScoped
@Singleton
public class VenueService extends BaseEntityService<Venue> {

    public VenueService() {
        super(Venue.class);
    }


    @GET
    @Path("/layouts/{layoutId:[0-9][0-9]*}/sections")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Section> getSections( @PathParam("layoutId") Long layoutId) {

        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Section> criteriaQuery = criteriaBuilder.createQuery(Section.class);
        Root<Section> root = criteriaQuery.from(Section.class);
        Predicate layoutCondition = criteriaBuilder.equal(root.get("layout").get("id"), layoutId);
        criteriaQuery.select(criteriaBuilder.createQuery(Section.class).getSelection()).where(layoutCondition);
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

}