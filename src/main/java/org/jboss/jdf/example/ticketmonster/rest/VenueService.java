package org.jboss.jdf.example.ticketmonster.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.jdf.example.ticketmonster.model.Event;

@Path("/events")
@RequestScoped
@Stateful
public class VenueService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEvents() {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        final CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class).select(criteriaBuilder.createQuery(Event.class).getSelection());
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getEvent(@PathParam("id") Long id) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        final CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class).select(criteriaBuilder.createQuery(Event.class).getSelection()).where();
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        return em.createQuery(criteriaQuery).getResultList();
    }
}
