package com.johnakins.ticketplatform.reservation;

//import com.johnakins.ticketplatform.common.exception.ResourceNotFoundException;
import com.johnakins.ticketplatform.event.EventStatus;
import com.johnakins.ticketplatform.ticket.TicketType;
import com.johnakins.ticketplatform.ticket.TicketTypeRepository;
import com.johnakins.ticketplatform.reservation.dto.CreateReservationRequest;
import com.johnakins.ticketplatform.reservation.dto.ReservationResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketReservationService {

    private final TicketReservationRepository reservationRepository;
    private final TicketTypeRepository ticketTypeRepository;

    private static final int RESERVATION_MINUTES = 10;

    // 1. Create reservation
    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        // Lock the ticket type row.
        TicketType ticketType = ticketTypeRepository
                .findByIdForUpdate(request.ticketTypeId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found"
                        )
                );
        // Only published events can receive reservations.
        if (ticketType.getEvent().getStatus()
                != EventStatus.PUBLISHED) {

            throw new IllegalStateException(
                    "Tickets are not available for this event"
            );
        }
        // Reserve inventory.
        ticketType.reserve(request.quantity());
        // Create reservation.
        TicketReservation reservation = new TicketReservation();
        reservation.setTicketType(ticketType);
        reservation.setCustomerEmail(request.customerEmail());
        reservation.setQuantity(request.quantity());
        reservation.setUnitPrice(ticketType.getPrice());
        reservation.setTotalAmount(
                ticketType.getPrice()
                        .multiply(
                                java.math.BigDecimal.valueOf(
                                        request.quantity()
                                )
                        )
        );
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setExpiresAt(
                LocalDateTime.now().plusMinutes(
                        RESERVATION_MINUTES
                )
        );
        TicketReservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    // 2. Get reservation by ID
    @Transactional(readOnly = true)
    public ReservationResponse getReservation(UUID reservationId) {
        TicketReservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Reservation not found"
                                )
                        );
        return ReservationResponse.from(reservation);
    }

    // 3. Confirm reservation after successful payment
    @Transactional
    public void confirmReservation(UUID reservationId) {
        TicketReservation reservation = reservationRepository.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Reservation not found"
                                )
                        );
        // Idempotency: do not confirm twice.
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) return;
        if (reservation.getStatus() != ReservationStatus.PENDING) throw new IllegalStateException("Reservation is not pending");
        if (!reservation.getExpiresAt().isAfter(LocalDateTime.now())) throw new IllegalStateException("Reservation has expired");

        TicketType ticketType = ticketTypeRepository
                .findByIdForUpdate(
                        reservation.getTicketType().getId()
                )
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found"
                        )
                );
        // Move quantity from reserved to sold.
        ticketType.confirmReservation(reservation.getQuantity());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setConfirmedAt(LocalDateTime.now());
    }

    // 4. Release reservation after definite payment failure
    @Transactional
    public void releaseReservation(UUID reservationId) {
        TicketReservation reservation =
                reservationRepository.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Reservation not found"
                                )
                        );

        // Don't release a confirmed reservation.
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) throw new IllegalStateException("A confirmed reservation cannot be released");
        // Repeated release requests are harmless.
        if (reservation.getStatus() != ReservationStatus.PENDING) return;

        TicketType ticketType = ticketTypeRepository
                .findByIdForUpdate(
                        reservation.getTicketType().getId()
                )
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found"
                        )
                );
        ticketType.releaseReservation(reservation.getQuantity());

        reservation.setStatus(ReservationStatus.RELEASED);
        reservation.setReleasedAt(LocalDateTime.now());
    }

    // 5. Expire reservation after timeout
    @Transactional
    public void expireReservation(UUID reservationId) {
        TicketReservation reservation =
                reservationRepository.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "Reservation not found"
                                )
                        );

        if (reservation.getStatus() != ReservationStatus.PENDING) return;
        if (reservation.getExpiresAt().isAfter(LocalDateTime.now())) return;

        TicketType ticketType = ticketTypeRepository
                .findByIdForUpdate(
                        reservation.getTicketType().getId()
                )
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Ticket type not found"
                        )
                );
        ticketType.releaseReservation(reservation.getQuantity());

        reservation.setStatus(ReservationStatus.EXPIRED);
        reservation.setReleasedAt(LocalDateTime.now());
    }
}














