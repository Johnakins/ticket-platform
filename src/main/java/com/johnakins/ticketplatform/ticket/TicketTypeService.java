package com.johnakins.ticketplatform.ticket;

//import com.johnakins.ticketplatform.common.exception.ResourceNotFoundException;
import com.johnakins.ticketplatform.event.Event;
import com.johnakins.ticketplatform.event.EventRepository;
import com.johnakins.ticketplatform.event.EventStatus;
import com.johnakins.ticketplatform.ticket.dto.CreateTicketTypeRequest;
import com.johnakins.ticketplatform.ticket.dto.TicketTypeResponse;
import com.johnakins.ticketplatform.ticket.dto.UpdateTicketTypeRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;

    // 1. Create ticket type
    public TicketTypeResponse createTicketType(
            UUID eventId,
            CreateTicketTypeRequest request
    ) {

        Event event = findEvent(eventId);

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException(
                    "Ticket types can only be created for draft events"
            );
        }

        TicketType ticketType = new TicketType();

        ticketType.setEvent(event);
        ticketType.setName(request.name());
        ticketType.setPrice(request.price());
        ticketType.setTotalQuantity(request.totalQuantity());
        ticketType.setSoldQuantity(0);
        ticketType.setReservedQuantity(0);

        TicketType saved = ticketTypeRepository.save(ticketType);

        return TicketTypeResponse.from(saved);
    }

    // 2. Get ticket types for an event
    @Transactional(readOnly = true)
    public List<TicketTypeResponse> getEventTicketTypes(UUID eventId) {

        findEvent(eventId);

        return ticketTypeRepository.findByEventId(eventId)
                .stream()
                .map(TicketTypeResponse::from)
                .toList();
    }

    // 3. Get ticket type by ID
    @Transactional(readOnly = true)
    public TicketTypeResponse getTicketType(UUID ticketTypeId) {

        TicketType ticketType = findTicketType(ticketTypeId);

        return TicketTypeResponse.from(ticketType);
    }

    // 4. Update ticket type
    public TicketTypeResponse updateTicketType(
            UUID ticketTypeId,
            UpdateTicketTypeRequest request
    ) {

        TicketType ticketType = ticketTypeRepository
                .findByIdForUpdate(ticketTypeId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found: " + ticketTypeId
                        )
                );

        if (ticketType.getEvent().getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException(
                    "Ticket types can only be updated for draft events"
            );
        }

        int minimumQuantity =
                ticketType.getSoldQuantity()
                        + ticketType.getReservedQuantity();

        if (request.totalQuantity() < minimumQuantity) {
            throw new IllegalArgumentException(
                    "Total quantity cannot be less than tickets already sold and reserved: "
                            + minimumQuantity
            );
        }

        ticketType.setName(request.name());
        ticketType.setPrice(request.price());
        ticketType.setTotalQuantity(request.totalQuantity());

        return TicketTypeResponse.from(
                ticketTypeRepository.save(ticketType)
        );
    }

    private Event findEvent(UUID eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Event not found: " + eventId
                        )
                );
    }

    private TicketType findTicketType(UUID ticketTypeId) {

        return ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found: " + ticketTypeId
                        )
                );
    }
}
