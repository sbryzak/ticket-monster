package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents an event, which may have multiple performances with different dates and
 * venues.
 *
 * @author Shane Bryzak
 * @author Marius Bogoevici
 */
@Entity
public class Event implements Serializable {
    private static final long serialVersionUID = -7237875436163170627L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @NotNull @NotEmpty
    @Size(min = 5, max = 50, message = "An event's name must contain between 5 and 50 characters")
    private String name;

    @NotNull @NotEmpty
    @Size(min = 20, max = 1000, message = "An event's name must contain between 20 and 1000 characters")
    private String description;

    /**
     * TODO: revise this, consider whether the Event owns the picture or media items can be shared between events
     */
    @ManyToOne
    private MediaItem picture;

    @ManyToOne @NotNull
    private EventCategory category;

    private boolean major;

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

    public MediaItem getPicture() {
        return picture;
    }

    public void setPicture(MediaItem picture) {
        this.picture = picture;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public boolean isMajor() {
        return major;
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (name != null ? !name.equals(event.name) : event.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
