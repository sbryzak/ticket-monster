package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A section is a specific area within a venue layout.  A venue layout may
 * consist of multiple sections, each with its own pricing scheme.
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties({"layout", "sectionRows"})
public class Section implements Serializable {
    private static final long serialVersionUID = 4293585694763708395L;

    @Id
    @GeneratedValue
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
     * The total seating capacity of the section
     */
    @Min(0)
    private int capacity;

    /**
     * The layout to which this section belongs
     */
    @ManyToOne
    private VenueLayout layout;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    @NotEmpty
    private Set<Row> sectionRows = new HashSet<Row>();

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public VenueLayout getLayout() {
        return layout;
    }

    public void setLayout(VenueLayout layout) {
        this.layout = layout;
    }

    public Set<Row> getSectionRows() {
        return sectionRows;
    }

    public void setSectionRows(Set<Row> sectionRows) {
        this.sectionRows = sectionRows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (layout != null ? !layout.equals(section.layout) : section.layout != null) return false;
        if (name != null ? !name.equals(section.name) : section.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (layout != null ? layout.hashCode() : 0);
        return result;
    }

}
