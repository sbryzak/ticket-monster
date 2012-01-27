package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Represents a single seating layout for a venue.  A venue may be capable of
 * multiple seating layouts, depending on the type of event.  A layout can
 * contain many sections.
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties({"sections"})
public class VenueLayout implements Serializable {
    private static final long serialVersionUID = -6988617479016327717L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @ManyToOne
    private Venue venue;
    
    @OneToMany(mappedBy = "layout", cascade = ALL)
    private Set<Section> sections = new HashSet<Section>();

    private String name;

    private int capacity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VenueLayout that = (VenueLayout) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (venue != null ? !venue.equals(that.venue) : that.venue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = venue != null ? venue.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
