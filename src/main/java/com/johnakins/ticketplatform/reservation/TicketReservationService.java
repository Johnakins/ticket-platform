package com.johnakins.ticketplatform.reservation;


import com.johnakins.ticketplatform.ticket.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketReservationService {

    private final TicketTypeRepository types;
    private final TicketRepository tickets;
    private final TicketReservationRepository reservations;

    @Transactional
    public TicketReservation reserve(UUID ticketTypeId, String email, int quantity) {
        // 1. Lock the ticket type row
        TicketType ticketType = types
                .findByIdForUpdate(ticketTypeId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket type not found")
                );

        // 2. Check available tickets and reserve
        ticketType.reserve(quantity);

        // 3. Create reservation record
        TicketReservation reservation = new TicketReservation();

        reservation.setTicketTypeId(ticketTypeId);
        reservation.setCustomerEmail(email);
        reservation.setQuantity(quantity);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setExpiresAt(
                LocalDateTime.now().plusMinutes(10)
        );

        // 4. Save reservation
        return reservations.save(reservation);
    }

    @Transactional
    public List<Ticket> confirmReservation(UUID reservationId) {
        // 1. Lock the reservation
        TicketReservation reservation =
                reservations.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException("Reservation not found")
                        );

        // 2. Prevent duplicate confirmation
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return tickets.findByReservationId(reservationId);
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException(
                    "Reservation is not pending"
            );
        }

        // 3. Check expiry
        if (!reservation.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "Reservation has expired"
            );
        }

        // 4. Lock ticket inventory
        TicketType type = types.findByIdForUpdate(
                reservation.getTicketTypeId()
        ).orElseThrow(() ->
                new NoSuchElementException("Ticket type not found")
        );

        // 5. Move reserved quantity to sold quantity
        type.confirmReservation(reservation.getQuantity());

        // 6. Generate individual tickets
        List<Ticket> generatedTickets = new ArrayList<>();

        for (int i = 0; i < reservation.getQuantity(); i++) {

            String number = "TKT-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 12)
                            .toUpperCase();

            String token = UUID.randomUUID().toString();

            Ticket ticket = new Ticket();

            ticket.setTicketType(type);
            ticket.setPurchaserEmail(reservation.getCustomerEmail());
            ticket.setTicketNumber(number);
            ticket.setQrToken(token);
            ticket.setStatus(TicketStatus.VALID);
            ticket.setPurchasedAt(LocalDateTime.now());
            ticket.setReservation(reservation);

            ticket.setReservation(reservation);

            generatedTickets.add(ticket);
        }

        // 7. Save tickets
        List<Ticket> savedTickets = tickets.saveAll(generatedTickets);

        // 8. Update reservation
        reservation.setStatus(ReservationStatus.CONFIRMED);

        return savedTickets;
    }


    @Transactional
    public void releaseReservation(UUID reservationId) {

        // 1. Find and lock the reservation
        TicketReservation reservation =
                reservations.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException("Reservation not found")
                        );

        // 2. Prevent releasing an already processed reservation
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            return;
        }

        // 3. Lock the ticket type
        TicketType type = types.findByIdForUpdate(
                reservation.getTicketTypeId()
        ).orElseThrow(() ->
                new NoSuchElementException("Ticket type not found")
        );

        // 4. Return reserved tickets to available inventory
        type.releaseReservation(reservation.getQuantity());

        // 5. Update reservation status
        reservation.setStatus(ReservationStatus.RELEASED);
    }


    @Transactional
    public void expireReservation(UUID reservationId) {
        // 1. Lock the reservation
        TicketReservation reservation =
                reservations.findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new NoSuchElementException("Reservation not found")
                        );

        // 2. Check reservation status
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            return;
        }

        // 3. Check expiry time again
        if (reservation.getExpiresAt().isAfter(LocalDateTime.now())) {
            return;
        }

        // 4. Lock ticket inventory
        TicketType type = types.findByIdForUpdate(
                reservation.getTicketTypeId()
        ).orElseThrow(() ->
                new NoSuchElementException("Ticket type not found")
        );

        // 5. Release reserved inventory
        type.releaseReservation(reservation.getQuantity());

        // 6. Update reservation status
        reservation.setStatus(ReservationStatus.EXPIRED);
    }

}














