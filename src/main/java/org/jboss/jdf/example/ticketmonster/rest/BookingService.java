package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.model.Allocation;
import org.jboss.jdf.example.ticketmonster.model.AllocationTicketCategoryCount;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.PriceCategory;
import org.jboss.jdf.example.ticketmonster.model.Row;
import org.jboss.jdf.example.ticketmonster.model.Section;


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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBooking(BookingRequest bookingRequest) {
        Performance performance = getEntityManager().find(Performance.class, bookingRequest.getPerformance());

        Map<Long, TicketRequest> ticketsByCategories = new HashMap<Long, TicketRequest>();
        for (TicketRequest ticketRequest : bookingRequest.getTicketRequests()) {
            ticketsByCategories.put(ticketRequest.getPriceCategory(), ticketRequest);
        }
        List<PriceCategory> loadedPriceCategories = getEntityManager().createQuery("select p from PriceCategory p where p.id in :ids").setParameter("ids", ticketsByCategories.keySet()).getResultList();

        Map<Long, PriceCategory> priceCategoriesById = new HashMap<Long, PriceCategory>();

        for (PriceCategory loadedPriceCategory : loadedPriceCategories) {
            priceCategoriesById.put(loadedPriceCategory.getId(), loadedPriceCategory);
        }
        
        Booking booking = new Booking();
        booking.setContactEmail(bookingRequest.getEmail());
        Map<Long, Integer> ticketCountsPerSection = new LinkedHashMap<Long, Integer>();
        Map<Long, List<AllocationTicketCategoryCount>> ticketsPerCategory = new LinkedHashMap<Long, List<AllocationTicketCategoryCount>>();
        for (TicketRequest ticketRequest: ticketsByCategories.values()) {
            final PriceCategory priceCategory = priceCategoriesById.get(ticketRequest.getPriceCategory());
            final Long sectionId = priceCategory.getSection().getId();
            if (!ticketCountsPerSection.containsKey(sectionId)) {
                ticketCountsPerSection.put(sectionId, 0);
                ticketsPerCategory.put(sectionId, new ArrayList<AllocationTicketCategoryCount>());
            }
            ticketCountsPerSection.put(sectionId, ticketCountsPerSection.get(sectionId) + ticketRequest.getQuantity());
            ticketsPerCategory.get(sectionId).add(new AllocationTicketCategoryCount(priceCategory.getTicketCategory(), ticketRequest.getQuantity()));
        }
        for (Long sectionId : ticketCountsPerSection.keySet()) {
            int ticketCount = ticketCountsPerSection.get(sectionId);
            if (ticketCount == 0) {
                continue;
            }
            Section section = getEntityManager().find(Section.class, sectionId);
            Set<Row> rows = section.getSectionRows();
            Allocation createdAllocation = null;
            for (Row row : rows) {
                List<Allocation> allocations = (List<Allocation>) getEntityManager().createQuery("select a from Allocation a  where a.booking.performance.id = :perfId and a.row.id = :rowId").setParameter("perfId", bookingRequest.getPerformance()).setParameter("rowId", row.getId()).getResultList();
                if (allocations.size() > 0) {
                    int confirmedCandidate = 0;
                    int nextCandidate = 1;
                    for (Allocation allocation : allocations) {
                        if (allocation.getStartSeat() - nextCandidate >= ticketCount) {
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
                        createdAllocation.setQuantity(ticketCount);
                        createdAllocation.setEndSeat(nextCandidate + ticketCount - 1);
                        break;
                    }
                } else {
                    createdAllocation = new Allocation();
                    createdAllocation.setRow(row);
                    createdAllocation.setStartSeat(1);
                    createdAllocation.setEndSeat(ticketCount);
                    createdAllocation.setQuantity(ticketCount);
                    break;
                }
            }
            if (createdAllocation == null) {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
            createdAllocation.setTicketsPerCategory(ticketsPerCategory.get(sectionId));
            System.out.println("Allocating " + createdAllocation.getQuantity() + " tickets ");
            booking.addAllocation(createdAllocation);
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
