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
import javax.ws.rs.core.UriInfo;

import org.jboss.jdf.example.ticketmonster.model.Show;

/**
 * @author Marius Bogoevici
 */
@Path("/shows")
@ApplicationScoped
@Singleton
public class ShowService extends BaseEntityService<Show> {

    public ShowService() {
        super(Show.class);
    }

    @Override
    protected Predicate[] extractPredicates(MultivaluedMap<String,
            String> queryParameters,
            CriteriaBuilder criteriaBuilder,
            Root<Show> root) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (queryParameters.containsKey("venue")) {
            String venue = queryParameters.getFirst("venue");
            predicates.add(criteriaBuilder.equal(root.get("venue").get("id"), venue));
        }

        if (queryParameters.containsKey("event")) {
            String event = queryParameters.getFirst("event");
            predicates.add(criteriaBuilder.equal(root.get("event").get("id"), event));
        }


        return predicates.toArray(new Predicate[]{});
    }
}
