package com.johnakins.ticketplatform.ticket;
import com.johnakins.ticketplatform.reservation.TicketReservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter @Setter
@Entity @Table(name="tickets", uniqueConstraints={@UniqueConstraint(columnNames="ticket_number"),@UniqueConstraint(columnNames="qr_token")})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;
    @Column(nullable = false)
    private String purchaserEmail;
    @Column(name = "ticket_number", nullable = false, unique = true)
    private String ticketNumber;
    @Column(name = "qr_token", nullable = false, unique = true)
    private String qrToken;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.VALID;
    @Column(nullable = false)
    private LocalDateTime purchasedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private TicketReservation reservation;

    public Ticket(
            TicketType ticketType,
            TicketReservation reservation,
            String purchaserEmail,
            String ticketNumber,
            String qrToken
    ) {
        this.ticketType = ticketType;
        this.reservation = reservation;
        this.purchaserEmail = purchaserEmail;
        this.ticketNumber = ticketNumber;
        this.qrToken = qrToken;
        this.status = TicketStatus.VALID;
        this.purchasedAt = LocalDateTime.now();
    }
}
