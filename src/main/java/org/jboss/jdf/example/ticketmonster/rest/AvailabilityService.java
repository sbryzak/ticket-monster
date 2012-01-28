package org.jboss.jdf.example.ticketmonster.rest;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.jdf.example.ticketmonster.model.Section;

/**
 * @author Marius Bogoevici
 */
@Path("/availability")
@RequestScoped
@Stateful
public class AvailabilityService {

    @Inject
    EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isAvailable(@QueryParam("section") long sectionId,
                               @QueryParam("performance") long performanceId,
                               @QueryParam("quantity") int quantity) {

        final Query query = entityManager.createQuery("select sum(allocation.quantity) from Allocation allocation where " +
                "allocation.booking.performance.id = :performanceId and allocation.row.section.id = :sectionId");
        Section section = null;
        try {
            section = (Section) entityManager.createQuery("select s from Section s where s.id = :sectionId and exists(select p from Performance p where s.venue.id = p.show.venue.id and p.id = :performanceId)")
                    .setParameter("performanceId", performanceId)
                    .setParameter("sectionId", sectionId).getSingleResult();
        } catch (NoResultException e) {
            // there's no such section for the given performance (may consider an exception)
            throw new IllegalArgumentException("There's no such section for the given performace");
        }

        Long allocated = (Long) query
                .setParameter("performanceId", performanceId)
                .setParameter("sectionId", sectionId).getSingleResult();

        return ((section != null ? section.getCapacity() : 0) - (allocated != null ? allocated : 0)) > quantity;
    }
}
