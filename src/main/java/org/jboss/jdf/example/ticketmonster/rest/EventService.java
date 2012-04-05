package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.jdf.example.ticketmonster.model.Event;

@Path("/events")
@ApplicationScoped
@Singleton
public class EventService extends BaseEntityService<Event> {

    public EventService() {
        super(Event.class);
    }

    @Override
    protected Predicate[] extractPredicates(
            MultivaluedMap<String, String> queryParameters, 
            CriteriaBuilder criteriaBuilder, 
            Root<Event> root) {
        List<Predicate> predicates = new ArrayList<Predicate>() ;
        
        if (queryParameters.containsKey("category")) {
            String category = queryParameters.getFirst("category");
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), category));
        }
        if (queryParameters.containsKey("major")) {
            String major = queryParameters.getFirst("major");
            predicates.add(criteriaBuilder.equal(root.get("major"), Boolean.parseBoolean(major)));
        }
        
        return predicates.toArray(new Predicate[]{});
    }
}
