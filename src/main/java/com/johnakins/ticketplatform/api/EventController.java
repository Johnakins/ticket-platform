package com.johnakins.ticketplatform.api;

import com.johnakins.ticketplatform.event.*;
import com.johnakins.ticketplatform.ticket.*;
import com.johnakins.ticketplatform.event.dto.CreateEventRequest;
import com.johnakins.ticketplatform.event.dto.EventResponse;
import com.johnakins.ticketplatform.event.dto.UpdateEventRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // POST /api/events
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // GET /api/events
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(
                eventService.getAllEvents()
        );
    }

    // GET /api/events/{eventId}
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(
                eventService.getEventById(eventId)
        );
    }

    // PUT /api/events/{eventId}
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        return ResponseEntity.ok(
                eventService.updateEvent(eventId, request)
        );
    }

    // PATCH /api/events/{eventId}/publish
    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(
                eventService.publishEvent(eventId)
        );
    }

    // PATCH /api/events/{eventId}/cancel
    @PatchMapping("/{eventId}/cancel")
    public ResponseEntity<EventResponse> cancelEvent(
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(
                eventService.cancelEvent(eventId)
        );
    }

    // GET /api/events/search?keyword=technology
    @GetMapping("/search")
    public ResponseEntity<List<EventResponse>> searchEvents(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(
                eventService.searchPublishedEvents(keyword)
        );
    }
}
