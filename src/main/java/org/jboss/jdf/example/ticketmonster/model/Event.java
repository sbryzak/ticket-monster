package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    /**
     * TODO: revise this, consider whether the Event owns the picture or media items can be shared between events
     */
    @ManyToOne
    private MediaItem picture;

    @ManyToOne
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
}
