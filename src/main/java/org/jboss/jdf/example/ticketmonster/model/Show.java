package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * A show is an instance of an event taking plac at a particular venue. A show can have multiple
 * performances.
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties("priceCategories")
public class Show implements Serializable {

    private static final long serialVersionUID = -108405033615497885L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private VenueLayout venueLayout;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "show", cascade = {CascadeType.ALL})
    @NotEmpty
    private Set<Performance> performances = new HashSet<Performance>();

    @OneToMany(mappedBy = "show")
    @NotEmpty
    private Set<PriceCategory> priceCategories = new HashSet<PriceCategory>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(Set<Performance> performances) {
        this.performances = performances;
    }

    public VenueLayout getVenueLayout() {
        return venueLayout;
    }

    public void setVenueLayout(VenueLayout venueLayout) {
        this.venueLayout = venueLayout;
    }

    public Set<PriceCategory> getPriceCategories() {
        return priceCategories;
    }

    public void setPriceCategories(Set<PriceCategory> priceCategories) {
        this.priceCategories = priceCategories;
    }
}
