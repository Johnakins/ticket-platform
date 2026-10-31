package com.johnakins.ticketplatform.reservation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket_reservations")
@Getter
@Setter
public class TicketReservation {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private UUID ticketTypeId;

        private String customerEmail;

        private int quantity;

        @Enumerated(EnumType.STRING)
        private ReservationStatus status;

        private LocalDateTime expiresAt;

        private LocalDateTime createdAt;

        // Getters and setters

}

