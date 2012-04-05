package org.jboss.jdf.examples.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.jdf.example.ticketmonster.model.Venue;
import org.jboss.jdf.example.ticketmonster.model.MediaItem;

/**
 * Backing bean for Venue entities.
 * <p>
 * This class provides CRUD functionality for all Venue entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class VenueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Venue entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Venue venue;

	public Venue getVenue() {
		return this.venue;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		return "create?faces-redirect=true";
	}
	
	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
		}

		if (this.id == null) {
			this.venue = this.search;
		} else {
			this.venue = this.entityManager.find(Venue.class, getId());
		}
	}

	/*
	 * Support updating and deleting Venue entities
	 */

	public String update() {
		this.conversation.end();
		
		try {
			if (this.id == null) {
				this.entityManager.persist(this.venue);
				return "search?faces-redirect=true";			
			} else {
				this.entityManager.merge(this.venue);
				return "view?faces-redirect=true&id=" + this.venue.getId();
			}
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			this.entityManager.remove(this.entityManager.find(Venue.class,
					getId()));
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	/*
	 * Support searching Venue entities with pagination
	 */

	private int page;
	private long count;
	private List<Venue> pageItems;
	
	private Venue search = new Venue();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Venue getSearch() {
		return this.search;
	}

	public void setSearch(Venue search) {
		this.search = search;
	}

	public void search() {
		this.page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Venue> root = countCriteria.from(Venue.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Venue> criteria = builder.createQuery(Venue.class);
		root = criteria.from(Venue.class);
		TypedQuery<Venue> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Venue> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.search.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(root.<String>get("name"), '%' + name + '%'));
		}
		MediaItem picture = this.search.getPicture();
		if (picture != null) {
			predicatesList.add(builder.equal(root.get("picture"), picture));
		}
		String description = this.search.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(root.<String>get("description"), '%' + description + '%'));
		}
		int capacity = this.search.getCapacity();
		if (capacity != 0) {
			predicatesList.add(builder.equal(root.get("capacity"), capacity));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Venue> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Venue entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Venue> getAll() {

		CriteriaQuery<Venue> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Venue.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Venue.class))).getResultList();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return VenueBean.this.entityManager.find(Venue.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Venue) value).getId());
			}
		};
	}
	
	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
	 
	private Venue add = new Venue();

	public Venue getAdd() {
		return this.add;
	}

	public Venue getAdded() {
		Venue added = this.add;
		this.add = new Venue();
		return added;
	}
}