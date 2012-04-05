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

import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;

/**
 * Backing bean for SectionAllocation entities.
 * <p>
 * This class provides CRUD functionality for all SectionAllocation entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SectionAllocationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving SectionAllocation entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private SectionAllocation sectionAllocation;

	public SectionAllocation getSectionAllocation() {
		return this.sectionAllocation;
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
			this.sectionAllocation = this.search;
		} else {
			this.sectionAllocation = this.entityManager.find(SectionAllocation.class, getId());
		}
	}

	/*
	 * Support updating and deleting SectionAllocation entities
	 */

	public String update() {
		this.conversation.end();
		
		try {
			if (this.id == null) {
				this.entityManager.persist(this.sectionAllocation);
				return "search?faces-redirect=true";			
			} else {
				this.entityManager.merge(this.sectionAllocation);
				return "view?faces-redirect=true&id=" + this.sectionAllocation.getId();
			}
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			this.entityManager.remove(this.entityManager.find(SectionAllocation.class,
					getId()));
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	/*
	 * Support searching SectionAllocation entities with pagination
	 */

	private int page;
	private long count;
	private List<SectionAllocation> pageItems;
	
	private SectionAllocation search = new SectionAllocation();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public SectionAllocation getSearch() {
		return this.search;
	}

	public void setSearch(SectionAllocation search) {
		this.search = search;
	}

	public void search() {
		this.page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<SectionAllocation> root = countCriteria.from(SectionAllocation.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<SectionAllocation> criteria = builder.createQuery(SectionAllocation.class);
		root = criteria.from(SectionAllocation.class);
		TypedQuery<SectionAllocation> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<SectionAllocation> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<SectionAllocation> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back SectionAllocation entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<SectionAllocation> getAll() {

		CriteriaQuery<SectionAllocation> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(SectionAllocation.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(SectionAllocation.class))).getResultList();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return SectionAllocationBean.this.entityManager.find(SectionAllocation.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((SectionAllocation) value).getId());
			}
		};
	}
	
	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
	 
	private SectionAllocation add = new SectionAllocation();

	public SectionAllocation getAdd() {
		return this.add;
	}

	public SectionAllocation getAdded() {
		SectionAllocation added = this.add;
		this.add = new SectionAllocation();
		return added;
	}
}