package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


import org.jboss.jdf.example.ticketmonster.model.Allocation;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Customer;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.SectionRow;
import org.jboss.jdf.example.ticketmonster.model.VenueLayout;

/**
 * @author Marius Bogoevici
 */
@Path("/bookings")
@Stateful
@RequestScoped
public class BookingService {
   
    @Inject
    EntityManager entityManager;
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createBooking(MultivaluedMap<String, String> formInput) {
        String email = formInput.getFirst("email");
        Customer customer = new Customer();
        customer.setEmail(email);
        entityManager.persist(customer);
        Long performanceId = Long.valueOf(formInput.getFirst("performance"));
        Performance performance = entityManager.find(Performance.class, performanceId);
        VenueLayout venueLayout = performance.getShow().getVenueLayout();
        List<Section> sections = (List<Section>) entityManager.createQuery("select s from Section s where s.layout.id = :id").setParameter("id", venueLayout.getId()).getResultList();
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCreatedOn(new Date());
        booking.setAllocations(new ArrayList<Allocation>());
        for (Section section : sections) {
            final String ticketCountString = formInput.getFirst("section" + section.getId());
            if (ticketCountString == null) {
                // no tickets for this section
                continue;
            }
            Integer ticketCount = Integer.valueOf(ticketCountString);
            List<SectionRow> rows = (List<SectionRow>) entityManager.createQuery("select r from SectionRow r where r.section.id = :id").setParameter("id", section.getId()).getResultList();
            Allocation createdAllocation = null;
            for (SectionRow row : rows) {
                List<Allocation> allocations = (List<Allocation>) entityManager.createQuery("select a from Allocation a  where a.performance.id = :perfId and a.row.id = :rowId").setParameter("perfId", performanceId).setParameter("rowId", row.getId());
                if (allocations.size() > 0) {
                    int confirmedCandidate = 0;
                    int nextCandidate = 1;
                    for (Allocation allocation : allocations) {
                        if (allocation.getStartSeat() - nextCandidate >= ticketCount) {
                            System.out.println("Found row " + row.getName());
                            confirmedCandidate = nextCandidate;
                            break;
                        }
                        nextCandidate = allocation.getEndSeat() + 1;
                    }
                    if (confirmedCandidate == 0 && ((row.getCapacity() + 1) - nextCandidate) >= ticketCount) {
                        confirmedCandidate = nextCandidate;
                    }
                    if (confirmedCandidate != 0) {
                        createdAllocation = new Allocation();
                        createdAllocation.setRow(row);
                        createdAllocation.setStartSeat(nextCandidate);
                        createdAllocation.setEndSeat(nextCandidate+ticketCount+1);
                        break;
                    }
                }
                else {
                    System.out.println("Found row " + row.getName());
                    createdAllocation = new Allocation();
                    createdAllocation.setRow(row);
                    createdAllocation.setStartSeat(1);
                    createdAllocation.setEndSeat(ticketCount);
                }
            }
            if (createdAllocation == null) {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
            createdAllocation.setPerformance(performance);
            entityManager.persist(createdAllocation);
            booking.getAllocations().add(createdAllocation);
        }
        return Response.ok().build();
    }
}
