package org.jboss.jdf.example.ticketmonster.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.jboss.jdf.example.ticketmonster.model.Event;

/**
 * @author Marius Bogoevici
 */
public abstract class BaseEntityService<T> {

    @Inject
    private Logger log;
    @Inject
    private EntityManager em;

    private Class<T> entityClass;

    public BaseEntityService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<T> getAll(@Context UriInfo uriInfo) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate[] predicates = extractPredicates(uriInfo, criteriaBuilder, root);
        criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);
        final MultivaluedMap<String,String> queryParameters = uriInfo.getQueryParameters();
        
        TypedQuery<T> query = em.createQuery(criteriaQuery);
        if (queryParameters.containsKey("first")) {
        	Integer firstRecord = Integer.parseInt(queryParameters.getFirst("first"));
        	query.setFirstResult(firstRecord);
        }
        if (queryParameters.containsKey("maxResults")) {
        	Integer maxResults = Integer.parseInt(queryParameters.getFirst("maxResults"));
        	query.setMaxResults(maxResults);
        }
		return query.getResultList();
    }

    protected abstract Predicate[] extractPredicates(UriInfo uriInfo, CriteriaBuilder criteriaBuilder, Root<T> root);

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public T getSingleInstance(@PathParam("id") Long id) {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        Predicate condition = criteriaBuilder.equal(root.get("id"), id);
        criteriaQuery.select(criteriaBuilder.createQuery(entityClass).getSelection()).where(condition);
        return em.createQuery(criteriaQuery).getSingleResult();
    }
}
