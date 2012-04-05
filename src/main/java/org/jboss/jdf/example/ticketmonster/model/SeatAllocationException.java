package org.jboss.jdf.example.ticketmonster.model;

/**
 * @author Marius Bogoevici
 */
public class SeatAllocationException extends RuntimeException {

    public SeatAllocationException() {
    }

    public SeatAllocationException(String s) {
        super(s);
    }

    public SeatAllocationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SeatAllocationException(Throwable throwable) {
        super(throwable);
    }
}
