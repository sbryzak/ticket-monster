package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.jboss.jdf.example.ticketmonster.model.Allocation;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Customer;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.PriceCategory;
import org.jboss.jdf.example.ticketmonster.model.SectionRow;
import org.jboss.jdf.example.ticketmonster.model.VenueLayout;

/**
 * @author Marius Bogoevici
 */
@Path("/bookings")
@Stateful
@RequestScoped
public class BookingService extends BaseEntityService<Booking> {

    @Inject
    EntityManager entityManager;

    public BookingService() {
        super(Booking.class);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createBooking(@FormParam("email") String email,
                                  @FormParam("performance") Long performanceId,
                                  @FormParam("priceCategories") Long[] priceCategoryIds,
                                  @FormParam("sections") Long[] sectionIds,
                                  @FormParam("tickets") String[] ticketCounts
    ) {
        Customer customer = new Customer();
        customer.setEmail(email);
        entityManager.persist(customer);
        Performance performance = entityManager.find(Performance.class, performanceId);
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCreatedOn(new Date());
        booking.setAllocations(new ArrayList<Allocation>());
        if (ticketCounts.length != priceCategoryIds.length) {
            Map<String, String> entity = new HashMap<String, String>();
            entity.put("cause", "There must be as many pr as tickets");
            Response response = Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }
        Map<Long, Integer> ticketCountsPerSection = new LinkedHashMap<Long, Integer>();
        for (int i = 0; i < ticketCounts.length; i++) {
            if (ticketCounts[i] == null || "".equals(ticketCounts[i].trim()))
                continue;
            Integer ticketCountAsInteger = Integer.valueOf(ticketCounts[i]);
            if (!ticketCountsPerSection.containsKey(sectionIds[i])) {
                ticketCountsPerSection.put(sectionIds[i], 0);
            }
            ticketCountsPerSection.put(sectionIds[i], ticketCountsPerSection.get(sectionIds[i]) + ticketCountAsInteger);
        }
        for (Long sectionId : ticketCountsPerSection.keySet()) {
            int ticketCount = ticketCountsPerSection.get(sectionId);
            if (ticketCount == 0) {
                continue;
            }
            List<SectionRow> rows = (List<SectionRow>) entityManager.createQuery("select r from SectionRow r where r.section.id = :id").setParameter("id", sectionId).getResultList();
            Allocation createdAllocation = null;
            for (SectionRow row : rows) {
                List<Allocation> allocations = (List<Allocation>) entityManager.createQuery("select a from Allocation a  where a.performance.id = :perfId and a.row.id = :rowId").setParameter("perfId", performanceId).setParameter("rowId", row.getId()).getResultList();
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
            createdAllocation.setPerformance(performance);
            entityManager.persist(createdAllocation);
            booking.getAllocations().add(createdAllocation);
        }
        entityManager.persist(booking);
        return Response.ok().entity(booking).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
