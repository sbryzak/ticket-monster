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

import org.jboss.jdf.example.ticketmonster.model.MediaItem;
import org.jboss.jdf.example.ticketmonster.model.MediaType;

/**
 * Backing bean for MediaItem entities.
 * <p>
 * This class provides CRUD functionality for all MediaItem entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class MediaItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving MediaItem entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private MediaItem mediaItem;

	public MediaItem getMediaItem() {
		return this.mediaItem;
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
			this.mediaItem = this.search;
		} else {
			this.mediaItem = this.entityManager.find(MediaItem.class, getId());
		}
	}

	/*
	 * Support updating and deleting MediaItem entities
	 */

	public String update() {
		this.conversation.end();
		
		try {
			if (this.id == null) {
				this.entityManager.persist(this.mediaItem);
				return "search?faces-redirect=true";			
			} else {
				this.entityManager.merge(this.mediaItem);
				return "view?faces-redirect=true&id=" + this.mediaItem.getId();
			}
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			this.entityManager.remove(this.entityManager.find(MediaItem.class,
					getId()));
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch( Exception e ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( e.getMessage() ));
			return null;
		}
	}

	/*
	 * Support searching MediaItem entities with pagination
	 */

	private int page;
	private long count;
	private List<MediaItem> pageItems;
	
	private MediaItem search = new MediaItem();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public MediaItem getSearch() {
		return this.search;
	}

	public void setSearch(MediaItem search) {
		this.search = search;
	}

	public void search() {
		this.page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<MediaItem> root = countCriteria.from(MediaItem.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<MediaItem> criteria = builder.createQuery(MediaItem.class);
		root = criteria.from(MediaItem.class);
		TypedQuery<MediaItem> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<MediaItem> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		MediaType mediaType = this.search.getMediaType();
		if (mediaType != null) {
			predicatesList.add(builder.equal(root.get("mediaType"), mediaType));
		}
		String url = this.search.getUrl();
		if (url != null && !"".equals(url)) {
			predicatesList.add(builder.like(root.<String>get("url"), '%' + url + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<MediaItem> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back MediaItem entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<MediaItem> getAll() {

		CriteriaQuery<MediaItem> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(MediaItem.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(MediaItem.class))).getResultList();
	}

	public Converter getConverter() {

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return MediaItemBean.this.entityManager.find(MediaItem.class,
						Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((MediaItem) value).getId());
			}
		};
	}
	
	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
	 
	private MediaItem add = new MediaItem();

	public MediaItem getAdd() {
		return this.add;
	}

	public MediaItem getAdded() {
		MediaItem added = this.add;
		this.add = new MediaItem();
		return added;
	}
}