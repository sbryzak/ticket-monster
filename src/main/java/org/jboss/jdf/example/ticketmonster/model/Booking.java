package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Set;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * A Booking represents a set of tickets purchased for a performance.
 *
 * @author Marius Bogoevici
 */
@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "booking")
    @NotEmpty
    @Valid
    private Set<Allocation> allocations;

    @ManyToOne
    @Valid
    private Customer customer;

    @NotEmpty
    private String cancellationCode;

    @NotNull
    private Date createdOn;

    public Long getId() {
        return id;
    }

    public Set<Allocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(Set<Allocation> allocations) {
        this.allocations = allocations;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
}
