package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A section is a specific area within a venue layout.  A venue layout may
 * consist of multiple sections, each with its own pricing scheme.
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties({"venue", "sectionRows"})
public class Section implements Serializable {
    private static final long serialVersionUID = 4293585694763708395L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    /**
     * The short name of the section, may be a code such as A12, G7, etc.
     */
    private String name;

    /**
     * The description of the section, such as 'Rear Balcony', etc.
     */
    @NotEmpty
    private String description;

    /**
     * The layout to which this section belongs
     */
    @ManyToOne
    private Venue venue;

    private int numberOfRows;
    
    private int rowCapacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getRowCapacity() {
        return rowCapacity;
    }

    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }
    
    public int getCapacity() {
        return this.rowCapacity * this.numberOfRows;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (venue != null ? !venue.equals(section.venue) : section.venue != null) return false;
        if (name != null ? !name.equals(section.name) : section.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (venue != null ? venue.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
