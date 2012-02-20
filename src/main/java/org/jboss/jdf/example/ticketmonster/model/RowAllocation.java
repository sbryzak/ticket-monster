package org.jboss.jdf.example.ticketmonster.model;

import java.util.Arrays;

import javax.persistence.Embeddable;

/**
 * @author Marius Bogoevici
 */
@Embeddable
public class RowAllocation {

    private boolean[] occupied;

    /** private constructor for persistence */
    private RowAllocation() {
    }

    public RowAllocation(int capacity) {
        this.occupied = new boolean[capacity];
        Arrays.fill(occupied, false);
    }

    public int findFirstGapStart(int size) {
        int candidateStart = -1;
        for (int i=0; i< occupied.length; i++) {
           if (!occupied[i]) {
               if (candidateStart == -1) {
                 candidateStart = i;
               }
               if ((size == (i-candidateStart + 1))) {
                   return candidateStart;
               }
           }
           else {
               candidateStart = -1;
           }
        }
        return -1;
    }

    public void allocate(int start, int size) throws SeatAllocationException {
       if (size <= 0) {
           throw new SeatAllocationException("Number of seats must be greater than zero");
       }
       if (start < 0 || start >= occupied.length) {
           throw new SeatAllocationException("Seat number must be betwen 1 and " + occupied.length);
       }
       if ((start + size) > occupied.length) {
           throw new SeatAllocationException("Cannot allocate seats above row capacity");
       }
       for (int i=start; i<(start + size); i++) {
           if (occupied[i]) {
               throw new SeatAllocationException("Found occupied seats in the requested block");
           }
       }

        for (int i = start; i < (start + size); i++) {
            occupied[i] = true;
        }

    }
}
