package org.jboss.jdf.example.ticketmonster.services;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Seat;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;

/**
 * @author Marius Bogoevici
 */
public class SeatAllocationService implements Serializable {

    @Inject
    EntityManager entityManager;

    public List<Seat> allocateSeats(Section section, Performance performance, int seatCount, boolean contiguous) {
        SectionAllocation sectionAllocation = retrieveSectionAllocation(section, performance);
        return sectionAllocation.allocateSeats(seatCount, contiguous);
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