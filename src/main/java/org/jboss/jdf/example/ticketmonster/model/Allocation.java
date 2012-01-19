package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;

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
    private Performance performance;

    @ManyToOne
    private SectionRow row;

    private int startSeat;

    private int endSeat;

    public Long getId() {
        return id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public SectionRow getRow() {
        return row;
    }

    public void setRow(SectionRow row) {
        this.row = row;
    }

    public int getQuantity() {
        return endSeat - startSeat + 1;
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
}
