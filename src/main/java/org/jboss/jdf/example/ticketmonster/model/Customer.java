package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A Customer is a person who has purchased tickets.
 *
 * @author Marius Bogoevici
 */
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Email(message = "Not a valid email format")
    private String email;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
