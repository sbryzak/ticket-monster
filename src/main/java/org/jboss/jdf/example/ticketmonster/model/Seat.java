package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

/**
 * @author Marius Bogoevici
 */
@Embeddable
public class Seat {

    @ManyToOne
    private Row row;

    @Min(1)
    private int number;

    /** Constructor for persistence */
    private Seat() {
    }

    public Seat(Row row, int number) {
        this.row = row;
        this.number = number;
    }

    public Row getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }
}
