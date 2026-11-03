package com.johnakins.ticketplatform.reservation.dto;

import com.johnakins.ticketplatform.reservation.ReservationStatus;
import com.johnakins.ticketplatform.reservation.TicketReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(

        UUID id,
        UUID ticketTypeId,
        String ticketTypeName,
        String customerEmail,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        LocalDateTime confirmedAt

) {

    public static ReservationResponse from(
            TicketReservation reservation
    ) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getTicketType().getId(),
                reservation.getTicketType().getName(),
                reservation.getCustomerEmail(),
                reservation.getQuantity(),
                reservation.getUnitPrice(),
                reservation.getTotalAmount(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getExpiresAt(),
                reservation.getConfirmedAt()
        );
    }
}
