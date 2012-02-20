package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Row;
import org.jboss.jdf.example.ticketmonster.model.RowAllocation;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;
import org.jboss.jdf.example.ticketmonster.model.Ticket;
import org.jboss.jdf.example.ticketmonster.model.TicketCategory;
import org.jboss.jdf.example.ticketmonster.model.TicketPriceCategory;


/**
 * @author Marius Bogoevici
 */
@Path("/bookings")
@Stateful
@RequestScoped
public class BookingService extends BaseEntityService<Booking> {


    public BookingService() {
        super(Booking.class);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteBooking(@PathParam("id") Long id) {
        Booking booking = getEntityManager().find(Booking.class, id);
        if (booking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        getEntityManager().remove(booking);
        return Response.ok().build();
    }

    @SuppressWarnings("unchecked")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBooking(BookingRequest bookingRequest) {
        Performance performance = getEntityManager().find(Performance.class, bookingRequest.getPerformance());

        Set<Long> priceCategoryIds = new HashSet<Long>();
        for (TicketRequest ticketRequest : bookingRequest.getTicketRequests()) {
            if (priceCategoryIds.contains(ticketRequest.getPriceCategory())) {
                throw new RuntimeException("Duplicate price category id");
            }
            priceCategoryIds.add(ticketRequest.getPriceCategory());
        }
        List<TicketPriceCategory> ticketPrices = (List<TicketPriceCategory>) getEntityManager().createQuery("select p from TicketPriceCategory p where p.id in :ids").setParameter("ids", priceCategoryIds).getResultList();

        Map<Long, TicketPriceCategory> priceCategoriesById = new HashMap<Long, TicketPriceCategory>();

        for (TicketPriceCategory loadedPriceCategory : ticketPrices) {
            priceCategoriesById.put(loadedPriceCategory.getId(), loadedPriceCategory);
        }

        Booking booking = new Booking();
        booking.setContactEmail(bookingRequest.getEmail());
        
        Map<Section, Map<TicketCategory, TicketRequest>> ticketRequestsPerSection = new LinkedHashMap<Section, Map<TicketCategory, TicketRequest>>();
        for (TicketRequest ticketRequest : bookingRequest.getTicketRequests()) {
            final TicketPriceCategory priceCategory = priceCategoriesById.get(ticketRequest.getPriceCategory());
            if (!ticketRequestsPerSection.containsKey(priceCategory.getSection())) {
                ticketRequestsPerSection.put(priceCategory.getSection(), new LinkedHashMap<TicketCategory, TicketRequest>());
            }
            ticketRequestsPerSection.get(priceCategory.getSection()).put(priceCategoriesById.get(ticketRequest.getPriceCategory()).getTicketCategory(), ticketRequest);
        }

        for (Section section : ticketRequestsPerSection.keySet()) {
            SectionAllocation sectionAllocationStatus;
            try {
                sectionAllocationStatus = (SectionAllocation) getEntityManager().createQuery("select s from SectionAllocation s where s.performance.id = :performanceId and " +
                        " s.section.id = :sectionId").setParameter("performanceId", performance.getId()).setParameter("sectionId", section.getId()).getSingleResult();
            } catch (NoResultException e) {
                sectionAllocationStatus = new SectionAllocation(performance, section);
                getEntityManager().persist(sectionAllocationStatus);
            }
            final Map<TicketCategory, TicketRequest> requestsByCategory = ticketRequestsPerSection.get(section);
            int totalSeatsRequestedInSection = 0;
            for (TicketRequest ticketRequest : requestsByCategory.values()) {
               totalSeatsRequestedInSection += ticketRequest.getQuantity();
            }
            for (Row row : section.getSectionRows()) {
                RowAllocation rowAllocation = sectionAllocationStatus.getRowAllocations().get(row);
                int startSeat = rowAllocation.findFirstGapStart(totalSeatsRequestedInSection);
                rowAllocation.allocate(startSeat, totalSeatsRequestedInSection);
                if (startSeat >= 0) {
                    for (Map.Entry<TicketCategory, TicketRequest> requestEntry : requestsByCategory.entrySet()) {
                       for (int i=0 ; i<requestEntry.getValue().getQuantity(); i++) {
                           Ticket ticket = new Ticket();
                           ticket.setTicketCategory(requestEntry.getKey());
                           ticket.setPrice(priceCategoriesById.get(requestEntry.getValue().getPriceCategory()).getPrice());
                           ticket.setRow(row);
                           ticket.setSeatNumber(startSeat + i + 1);
                           getEntityManager().persist(ticket);
                           booking.getTickets().add(ticket);
                       }
                       startSeat += requestEntry.getValue().getQuantity();
                    }
                }
                break;
            }
        }
        booking.setPerformance(performance);
        booking.setCancellationCode("abc");
        getEntityManager().persist(booking);
        return Response.ok().entity(booking).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static class BookingRequest {

        private List<TicketRequest> ticketRequests = new ArrayList<TicketRequest>();
        private long performance;
        private String email;

        public List<TicketRequest> getTicketRequests() {
            return ticketRequests;
        }

        public void setTicketRequests(List<TicketRequest> ticketRequests) {
            this.ticketRequests = ticketRequests;
        }

        public long getPerformance() {
            return performance;
        }

        public void setPerformance(long performance) {

            this.performance = performance;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    public static class TicketRequest {

        private long priceCategory;

        private int quantity;


        public long getPriceCategory() {
            return priceCategory;
        }

        public void setPriceCategory(long priceCategory) {
            this.priceCategory = priceCategory;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
