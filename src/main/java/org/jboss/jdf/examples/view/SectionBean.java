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

import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.Venue;

/**
 * Backing bean for Section entities.
 * <p>
 * This class provides CRUD functionality for all Section entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Section entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Section section;

	public Section getSection() {
		return this.section;
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
			this.section = this.search;
		} else {
			this.section = this.entityManager.find(Section.class, getId());
		}
	}

	/*
	 * Support updating and deleting Section entities
	 */

	public String update() {
		this.conversation.end();
		
		try {
			if (this.id == null) {
				this.entityManager.persist(this.section);
				return "search?faces-redirect=true";			
			} else {
				this.entityManager.merge(this.section);
				return "view?faces-redirect=true&id=" + this.section.getId();
			}
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			this.entityManager.remove(this.entityManager.find(Section.class,
					getId()));
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	/*
	 * Support searching Section entities with pagination
	 */

	private int page;
	private long count;
	private List<Section> pageItems;
	
	private Section search = new Section();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Section getSearch() {
		return this.search;
	}

	public void setSearch(Section search) {
		this.search = search;
	}

	public void search() {
		this.page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Section> root = countCriteria.from(Section.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Section> criteria = builder.createQuery(Section.class);
		root = criteria.from(Section.class);
		TypedQuery<Section> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Section> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.search.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(root.<String>get("name"), '%' + name + '%'));
		}
		String description = this.search.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(root.<String>get("description"), '%' + description + '%'));
		}
		int numberOfRows = this.search.getNumberOfRows();
		if (numberOfRows != 0) {
			predicatesList.add(builder.equal(root.get("numberOfRows"), numberOfRows));
		}
		int rowCapacity = this.search.getRowCapacity();
		if (rowCapacity != 0) {
			predicatesList.add(builder.equal(root.get("rowCapacity"), rowCapacity));
		}
		Venue venue = this.search.getVenue();
		if (venue != null) {
			predicatesList.add(builder.equal(root.get("venue"), venue));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Section> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Section entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Section> getAll() {

		CriteriaQuery<Section> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Section.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Section.class))).getResultList();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return SectionBean.this.entityManager.find(Section.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Section) value).getId());
			}
		};
	}
	
	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
	 
	private Section add = new Section();

	public Section getAdd() {
		return this.add;
	}

	public Section getAdded() {
		Section added = this.add;
		this.add = new Section();
		return added;
	}
}