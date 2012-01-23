package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
* @author Marius Bogoevici
*/
@Embeddable
public class TicketCategoryCount {

    @ManyToOne
    private TicketCategory ticketCategory;

    private int ticketCount;

    public TicketCategoryCount(TicketCategory ticketCategory, int ticketCount) {
        this.ticketCategory = ticketCategory;
        this.ticketCount = ticketCount;
    }

    public TicketCategoryCount() {
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }
}
