package com.johnakins.ticketplatform.ticket.dto;

import com.johnakins.ticketplatform.ticket.Ticket;
import com.johnakins.ticketplatform.ticket.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String ticketNumber,
        String qrCodeToken,
        String purchaserEmail,
        String ticketType,
        TicketStatus status,
        LocalDateTime purchasedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTicketNumber(),
                ticket.getQrToken(),
                ticket.getPurchaserEmail(),
                ticket.getTicketType().getName(),
                ticket.getStatus(),
                ticket.getPurchasedAt()
        );
    }
}
