package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Date;

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

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Allocation> allocations;

    @ManyToOne
    private Customer customer;

    private Date createdOn;

    public Long getId() {
        return id;
    }

    public Collection<Allocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(Collection<Allocation> allocations) {
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

}
