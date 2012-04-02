package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Seat;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.Ticket;
import org.jboss.jdf.example.ticketmonster.model.TicketCategory;
import org.jboss.jdf.example.ticketmonster.model.TicketPriceCategory;
import org.jboss.jdf.example.ticketmonster.services.SeatAllocationService;


/**
 * @author Marius Bogoevici
 */
@Path("/bookings")
@Stateful
@RequestScoped
public class BookingService extends BaseEntityService<Booking> {

    @Inject
    SeatAllocationService seatAllocationService;

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
        try {
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
                int totalTicketsRequestedPerSection = 0;
                final Map<TicketCategory, TicketRequest> ticketRequestsByCategories = ticketRequestsPerSection.get(section);
                for (TicketRequest ticketRequest : ticketRequestsByCategories.values()) {
                    totalTicketsRequestedPerSection += ticketRequest.getQuantity();
                }
                List<Seat> seats = seatAllocationService.allocateSeats(section, performance, totalTicketsRequestedPerSection, true);
                int seatCounter = 0;
                for (TicketCategory ticketCategory : ticketRequestsByCategories.keySet()) {
                    final TicketRequest ticketRequest = ticketRequestsByCategories.get(ticketCategory);
                    final TicketPriceCategory ticketPriceCategory = priceCategoriesById.get(ticketRequest.getPriceCategory());
                    for (int i = 0; i < ticketRequest.getQuantity(); i++) {
                        Ticket ticket = new Ticket(seats.get(seatCounter + i), ticketCategory, ticketPriceCategory.getPrice());
                        getEntityManager().persist(ticket);
                        booking.getTickets().add(ticket);
                    }
                    seatCounter += ticketRequest.getQuantity();
                }
            }
            booking.setPerformance(performance);
            booking.setCancellationCode("abc");
            getEntityManager().persist(booking);
            return Response.ok().entity(booking).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (ConstraintViolationException e) {
            Map<String, Object> errors = new HashMap<String, Object>();
            List<String> errorMessages = new ArrayList<String>();
            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                errorMessages.add(constraintViolation.getMessage());
            }
            errors.put("errors", errorMessages);
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        } catch (Exception e) {
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("errors", Collections.singletonList(e.getMessage()));
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
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
