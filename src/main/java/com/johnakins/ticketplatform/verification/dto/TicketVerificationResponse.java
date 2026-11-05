package com.johnakins.ticketplatform.verification.dto;

import com.johnakins.ticketplatform.ticket.TicketStatus;

import java.util.UUID;

public record TicketVerificationResponse(
        boolean valid,
        String message,
        UUID ticketId,
        String ticketNumber,
        TicketStatus status
) {
}
