package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Contains price categories - each category represents the price for a ticket
 * in a particular section at a particular venue for a particular event, for a
 * particular ticket category.
 *
 * @author Shane Bryzak
 */
@Entity @JsonIgnoreProperties("show")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"SECTION_ID","SHOW_ID","TICKETCATEGORY_ID"}))
public class TicketPriceCategory implements Serializable {
    private static final long serialVersionUID = 6649855367578381386L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Section section;

    @ManyToOne
    private TicketCategory ticketCategory;

    private float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketPriceCategory that = (TicketPriceCategory) o;

        if (section != null ? !section.equals(that.section) : that.section != null) return false;
        if (show != null ? !show.equals(that.show) : that.show != null) return false;
        if (ticketCategory != null ? !ticketCategory.equals(that.ticketCategory) : that.ticketCategory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = show != null ? show.hashCode() : 0;
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + (ticketCategory != null ? ticketCategory.hashCode() : 0);
        return result;
    }
}
