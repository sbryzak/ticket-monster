package org.jboss.jdf.example.ticketmonster.rest;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.Validator;
import javax.ws.rs.Path;

import org.jboss.jdf.example.ticketmonster.model.Member;

@Path("/events")
@RequestScoped
@Stateful
public class EventService {
    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;
}
