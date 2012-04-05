package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A lookup table containing the various ticket categories.  E.g. Adult, Child,
 * Pensioner, etc.
 *
 * @author Shane Bryzak
 */
@Entity
public class TicketCategory implements Serializable {
    private static final long serialVersionUID = 6591486291129475067L;


    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @NotNull @NotEmpty
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        TicketCategory that = (TicketCategory) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
