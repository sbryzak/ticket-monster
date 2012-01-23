package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents a single row of seats within a section.  May also be used to
 * represent a table, for events such as dinner shows.
 * <p/>
 * Seat allocations within the row are given a number, starting with 1.
 *
 * @author Shane Bryzak
 * @author Marius Bogoevici
 */
@Entity
public class Row implements Serializable {
    private static final long serialVersionUID = 8180924487630451004L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull @NotEmpty
    private String name;

    @Min(0)
    private int capacity;


    @ManyToOne
    private Section section;

    public Long getId() {
        return id;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
