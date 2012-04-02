package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A Booking represents a set of tickets purchased for a performance.
 *
 * @author Marius Bogoevici
 */
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @OneToMany(fetch = EAGER,cascade = ALL)
    @JoinColumn(name = "booking_id")
    @NotEmpty
    @Valid
    private Set<Ticket> tickets = new HashSet<Ticket>();

    @ManyToOne
    private Performance performance;

    @NotEmpty
    private String cancellationCode;

    @NotNull
    private Date createdOn = new Date();

    @NotNull
    @NotEmpty
    @Email(message = "Not a valid email format")
    private String contactEmail;

    public Long getId() {
        return id;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public float getTotalTicketPrice() {
        float totalPrice = 0.0f;
        for (Ticket ticket : tickets) {
            totalPrice += (ticket.getPrice());
        }
        return totalPrice;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public void setCancellationCode(String cancellationCode) {
        this.cancellationCode = cancellationCode;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != null ? !id.equals(booking.id) : booking.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
