package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * An allocation consists of one or more contiguous sold seats
 * within a SectionRow.
 *
 * @author Shane Bryzak
 * @author Marius Bogoevici
 */
@Entity
public class Allocation implements Serializable {
    private static final long serialVersionUID = 8738724150877088864L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Row row;

    @ManyToOne
    private Booking booking;

    @ElementCollection
    public Set<AllocationTicketCategoryCount> ticketsPerCategory = new HashSet<AllocationTicketCategoryCount>();

    private int startSeat;

    private int endSeat;


    private int quantity;

    public Long getId() {
        return id;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStartSeat() {
        return startSeat;
    }

    public void setStartSeat(int startSeat) {
        this.startSeat = startSeat;
    }

    public int getEndSeat() {
        return endSeat;
    }

    public void setEndSeat(int endSeat) {
        this.endSeat = endSeat;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Set<AllocationTicketCategoryCount> getTicketsPerCategory() {
        return ticketsPerCategory;
    }

    public void setTicketsPerCategory(Set<AllocationTicketCategoryCount> ticketsPerCategory) {
        this.ticketsPerCategory = ticketsPerCategory;
    }


}
