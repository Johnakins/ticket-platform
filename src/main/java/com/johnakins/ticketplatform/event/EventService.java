package com.johnakins.ticketplatform.event;

//import com.johnakins.ticketplatform.common.exception.ResourceNotFoundException;
import com.johnakins.ticketplatform.event.dto.CreateEventRequest;
import com.johnakins.ticketplatform.event.dto.EventResponse;
import com.johnakins.ticketplatform.event.dto.UpdateEventRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    // 1. Create event
    public EventResponse createEvent(CreateEventRequest request) {

        validateDates(request.startDate(), request.endDate());

        Event event = new Event();

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());
        event.setStatus(EventStatus.DRAFT);

        Event savedEvent = eventRepository.save(event);

        return EventResponse.from(savedEvent);
    }

    // 2. Get all events
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {

        return eventRepository.findAll()
                .stream()
                .map(EventResponse::from)
                .toList();
    }

    // 3. Get event by ID
    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID eventId) {

        Event event = findEvent(eventId);

        return EventResponse.from(event);
    }

    // 4. Update event
    public EventResponse updateEvent(
            UUID eventId,
            UpdateEventRequest request
    ) {

        Event event = findEvent(eventId);

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft events can be updated"
            );
        }

        validateDates(request.startDate(), request.endDate());

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());

        return EventResponse.from(eventRepository.save(event));
    }

    // 5. Publish event
    public EventResponse publishEvent(UUID eventId) {

        Event event = findEvent(eventId);

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft events can be published"
            );
        }

        if (!event.getStartDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "Event start date must be in the future"
            );
        }

        if (!event.getEndDate().isAfter(event.getStartDate())) {
            throw new IllegalStateException(
                    "Event end date must be after start date"
            );
        }

        event.setStatus(EventStatus.PUBLISHED);

        return EventResponse.from(eventRepository.save(event));
    }

    // 6. Cancel event
    public EventResponse cancelEvent(UUID eventId) {

        Event event = findEvent(eventId);

        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Event is already cancelled"
            );
        }

        event.setStatus(EventStatus.CANCELLED);

        return EventResponse.from(eventRepository.save(event));
    }

    // 7. Search published events
    @Transactional(readOnly = true)
    public List<EventResponse> searchPublishedEvents(String keyword) {

        List<Event> events;

        if (keyword == null || keyword.isBlank()) {
            events = eventRepository.findByStatus(
                    EventStatus.PUBLISHED
            );
        } else {
            events = eventRepository
                    .findByStatusAndTitleContainingIgnoreCase(
                            EventStatus.PUBLISHED,
                            keyword.trim()
                    );
        }

        return events.stream()
                .map(EventResponse::from)
                .toList();
    }

    private Event findEvent(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Event not found: " + eventId
                        )
                );
    }

    private void validateDates(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "Event end date must be after start date"
            );
        }
    }
}