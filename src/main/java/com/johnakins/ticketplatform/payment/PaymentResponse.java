package com.johnakins.ticketplatform.payment;


import com.johnakins.ticketplatform.ticket.dto.TicketResponse;

import java.util.List;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID reservationId,
        String reference,
        PaymentStatus status,
        String failureReason,
        List<TicketResponse> tickets
) {

    public static PaymentResponse from(
            Payment payment,
            List<TicketResponse> tickets
    ) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReservationId(),
                payment.getReference(),
                payment.getStatus(),
                payment.getFailureReason(),
                tickets
        );
    }
}

