package com.johnakins.ticketplatform.api;

import com.johnakins.ticketplatform.ticket.TicketTypeService;
import com.johnakins.ticketplatform.ticket.dto.CreateTicketTypeRequest;
import com.johnakins.ticketplatform.ticket.dto.TicketTypeResponse;
import com.johnakins.ticketplatform.ticket.dto.UpdateTicketTypeRequest;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    // POST /api/events/{eventId}/ticket-types
    @PostMapping("/api/events/{eventId}/ticket-types")
    public ResponseEntity<TicketTypeResponse> createTicketType(
            @PathVariable UUID eventId,
            @Valid @RequestBody CreateTicketTypeRequest request
    ) {

        TicketTypeResponse response =
                ticketTypeService.createTicketType(eventId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // GET /api/events/{eventId}/ticket-types
    @GetMapping("/api/events/{eventId}/ticket-types")
    public ResponseEntity<List<TicketTypeResponse>> getEventTicketTypes(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                ticketTypeService.getEventTicketTypes(eventId)
        );
    }

    // GET /api/ticket-types/{ticketTypeId}
    @GetMapping("/api/ticket-types/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponse> getTicketType(
            @PathVariable UUID ticketTypeId
    ) {

        return ResponseEntity.ok(
                ticketTypeService.getTicketType(ticketTypeId)
        );
    }

    // PUT /api/ticket-types/{ticketTypeId}
    @PutMapping("/api/ticket-types/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponse> updateTicketType(
            @PathVariable UUID ticketTypeId,
            @Valid @RequestBody UpdateTicketTypeRequest request
    ) {

        return ResponseEntity.ok(
                ticketTypeService.updateTicketType(ticketTypeId, request)
        );
    }
}
