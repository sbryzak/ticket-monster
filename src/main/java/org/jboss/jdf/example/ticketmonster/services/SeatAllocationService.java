package org.jboss.jdf.example.ticketmonster.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Row;
import org.jboss.jdf.example.ticketmonster.model.RowAllocation;
import org.jboss.jdf.example.ticketmonster.model.Seat;
import org.jboss.jdf.example.ticketmonster.model.SeatAllocationException;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;

/**
 * @author Marius Bogoevici
 */
public class SeatAllocationService implements Serializable {

    @Inject
    EntityManager entityManager;

    public List<Seat> allocateSeats(Section section, Performance performance, int seatCount, boolean contiguous) {
        List<Seat> seats = new ArrayList<Seat>();
        SectionAllocation sectionAllocation = retrieveSectionAllocation(section, performance);
        for (Row row : section.getSectionRows()) {
            RowAllocation rowAllocation = sectionAllocation.getRowAllocations().get(row);
            if (contiguous) {
                int startSeat = rowAllocation.findFreeGapStart(0, seatCount);
                if (startSeat >= 0) {
                    rowAllocation.allocate(startSeat, seatCount);
                    for (int i = 1; i <= seatCount; i++) {
                        seats.add(new Seat(row, startSeat + i));
                    }
                    break;
                }
            } else {
                int startSeat = rowAllocation.findFreeGapStart(0, 1);
                if (startSeat >= 0) {
                    do {
                        seats.add(new Seat(row, startSeat + 1));
                        rowAllocation.allocate(startSeat, 1);
                        startSeat = rowAllocation.findFreeGapStart(startSeat, 1);
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

    private SectionAllocation retrieveSectionAllocation(Section section, Performance performance) {
        SectionAllocation sectionAllocationStatus;
        try {
            sectionAllocationStatus = (SectionAllocation) entityManager.createQuery("select s from SectionAllocation s where s.performance.id = :performanceId and " +
                    " s.section.id = :sectionId").setParameter("performanceId", performance.getId()).setParameter("sectionId", section.getId()).getSingleResult();
        } catch (NoResultException e) {
            sectionAllocationStatus = new SectionAllocation(performance, section);
            entityManager.persist(sectionAllocationStatus);
        }
        return sectionAllocationStatus;
    }
}