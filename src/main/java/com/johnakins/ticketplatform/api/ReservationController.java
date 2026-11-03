package com.johnakins.ticketplatform.api;

import com.johnakins.ticketplatform.reservation.TicketReservationService;
import com.johnakins.ticketplatform.reservation.dto.CreateReservationRequest;
import com.johnakins.ticketplatform.reservation.dto.ReservationResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final TicketReservationService reservationService;

    // POST /api/reservations
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // GET /api/reservations/{reservationId}
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(
            @PathVariable UUID reservationId
    ) {
        return ResponseEntity.ok(
                reservationService.getReservation(reservationId)
        );
    }

    // POST /api/reservations/{reservationId}/release
    @PostMapping("/{reservationId}/release")
    public ResponseEntity<Void> releaseReservation(
            @PathVariable UUID reservationId
    ) {
        reservationService.releaseReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
