package org.jboss.jdf.example.ticketmonster.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * Represents a single venue
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties("layouts")
public class Venue implements Serializable {
    private static final long serialVersionUID = -6588912817518967721L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private String description;
    
    @OneToMany(mappedBy="venue")
    private Set<VenueLayout> layouts;

    @ManyToOne
    @JoinColumn(name = "PICTURE_ID")
    private MediaItem picture;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public MediaItem getPicture() {
        return picture;
    }

    public void setPicture(MediaItem description) {
        this.picture = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<VenueLayout> getLayouts() {
      return layouts;
   }
    
    public void setLayouts(Set<VenueLayout> layouts) {
      this.layouts = layouts;
   }
}
