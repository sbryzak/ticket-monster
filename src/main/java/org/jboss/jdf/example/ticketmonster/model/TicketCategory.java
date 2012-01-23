package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @GeneratedValue
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
}
