package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sun.xml.internal.ws.api.FeatureConstructor;

/**
 * A show is a single instance of an event, at a particular time and venue.
 * 
 * @author Shane Bryzak
 *
 */
@Entity
public class Show implements Serializable
{
   private static final long serialVersionUID = -108405033615497885L;
   
   private Long id;
   private Event event;
   private Venue venue;
   private VenueLayout venueLayout;

   private List<Performance> performances;
   
   @Id @GeneratedValue
   public Long getId()
   {
      return id;
   }
   
   public void setId(Long id)
   {
      this.id = id;
   }
   
   @ManyToOne @JoinColumn(name = "EVENT_ID")
   public Event getEvent()
   {
      return event;
   }
   
   public void setEvent(Event event)
   {
      this.event = event;
   }
   
   @ManyToOne @JoinColumn(name = "VENUE_ID")
   public Venue getVenue()
   {
      return venue;
   }

   
   public void setVenue(Venue venue)
   {
      this.venue = venue;
   }

    @OneToMany(fetch = FetchType.EAGER) @JoinColumn(name = "SHOW_ID")
    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
   
   @ManyToOne @JoinColumn(name = "LAYOUT_ID")
   public VenueLayout getVenueLayout()
   {
      return venueLayout;
   }
   
   public void setVenueLayout(VenueLayout venueLayout)
   {
      this.venueLayout = venueLayout;
   }
   
}
