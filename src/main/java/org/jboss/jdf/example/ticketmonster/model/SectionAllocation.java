package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 * Represents the state of ticket allocation in a section, for a specific performance.
 *
 * Optimistic locking ensures that two tickets will not be sold within the same row
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"performance_id", "section_id"}))
public class SectionAllocation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Version
    private long version;

    @ManyToOne
    private Performance performance;

    @ManyToOne
    private Section section;

    @Lob
    private boolean allocated[][];
    


    public SectionAllocation() {
    }

    public SectionAllocation(Performance performance, Section section) {
        this.performance = performance;
        this.section = section;
        this.allocated = new boolean[section.getNumberOfRows()][section.getRowCapacity()];
    }


    public Performance getPerformance() {
        return performance;
    }

    public Section getSection() {
        return section;
    }

    public boolean isAllocated(Seat s) {
        return allocated[s.getRowNumber()-1][s.getNumber()-1];
    }

    public List<Seat> allocateSeats(int seatCount, boolean contiguous) {
        List<Seat> seats = new ArrayList<Seat>();
        for (int rowCounter = 0; rowCounter < section.getNumberOfRows(); rowCounter ++) {

            if (contiguous) {
                int startSeat = findFreeGapStart(rowCounter, 0, seatCount);
                if (startSeat >= 0) {
                    allocate(rowCounter, startSeat, seatCount);
                    for (int i = 1; i <= seatCount; i++) {
                        seats.add(new Seat(section, rowCounter + 1, startSeat + i));
                    }
                    break;
                }
            } else {
                int startSeat = findFreeGapStart(rowCounter, 0, 1);
                if (startSeat >= 0) {
                    do {
                        seats.add(new Seat(section, rowCounter + 1, startSeat + 1));
                        allocate(rowCounter, startSeat, 1);
                        startSeat = findFreeGapStart(rowCounter, startSeat, 1);
                    } while (startSeat >= 0 && seats.size() < seatCount);
                    if (seats.size() == seatCount) {
                        break;
                    }
                }
            }
        }
        if (seats.size() == seatCount) {
            return seats;
        } else {
            throw new SeatAllocationException("Cannot find the requested amount of seats");
        }
    }

    public int findFreeGapStart(int row, int startSeat, int size) {
        boolean[] occupied = allocated[row];
        int candidateStart = -1;
        for (int i=startSeat; i< occupied.length; i++) {
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

    public void allocate(int row, int start, int size) throws SeatAllocationException {
        boolean[] occupied = allocated[row];
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
    
    public long getId() {
        return id;
    }
}
