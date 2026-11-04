package com.johnakins.ticketplatform.ticket;
import com.johnakins.ticketplatform.event.*;
import com.johnakins.ticketplatform.reservation.ReservationStatus;
import com.johnakins.ticketplatform.reservation.TicketReservation;
import com.johnakins.ticketplatform.reservation.TicketReservationRepository;
import com.johnakins.ticketplatform.ticket.dto.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public List<TicketResponse> generateTickets(
            TicketReservation reservation) {

        // Prevent duplicate ticket generation.
        List<Ticket> existingTickets =
                ticketRepository.findByReservationId(reservation.getId());

        if (!existingTickets.isEmpty()) {
            return existingTickets.stream()
                    .map(TicketResponse::from)
                    .toList();
        }

        List<Ticket> tickets = new ArrayList<>();

        for (int i = 0; i < reservation.getQuantity(); i++) {

            String ticketNumber = generateTicketNumber();
            String qrToken = UUID.randomUUID().toString();

            Ticket ticket = new Ticket(
                    reservation.getTicketType(),
                    reservation,
                    reservation.getCustomerEmail(),
                    ticketNumber,
                    qrToken
            );

            tickets.add(ticket);
        }

        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);

        return savedTickets.stream()
                .map(TicketResponse::from)
                .toList();
    }

    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}