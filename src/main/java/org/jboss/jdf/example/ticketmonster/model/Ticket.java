package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * An allocation consists of one or more contiguous sold seats
 * within a SectionRow.
 *
 * @author Shane Bryzak
 * @author Marius Bogoevici
 */
@Entity
public class Ticket implements Serializable {
    private static final long serialVersionUID = 8738724150877088864L;

    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;

    @ManyToOne
    private Row row;

    private TicketCategory ticketCategory;

    private float price;

    private int seatNumber;

    public Long getId() {
        return id;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
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

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}

