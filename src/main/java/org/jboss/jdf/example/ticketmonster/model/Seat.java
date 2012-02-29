package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * @author Marius Bogoevici
 */
@Embeddable
public class Seat {

    @Min(1)
    private int rowNumber;

    @Min(1)
    private int number;

    @ManyToOne
    private Section section;

    /** Constructor for persistence */
    protected Seat() {
    }

    public Seat(Section section, int rowNumber, int number) {
        this.section = section;
        this.rowNumber = rowNumber;
        this.number = number;
    }

    public Section getSection() {
        return section;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getNumber() {
        return number;
    }
}
