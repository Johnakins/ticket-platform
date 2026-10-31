package com.johnakins.ticketplatform.ticket.dto;

import com.johnakins.ticketplatform.ticket.TicketType;
import com.johnakins.ticketplatform.ticket.TicketTypeStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponse(

        UUID id,
        UUID eventId,
        String name,
        BigDecimal price,
        int totalQuantity,
        int soldQuantity,
        int reservedQuantity,
        int availableQuantity,
        TicketTypeStatus status

) {

    public static TicketTypeResponse from(TicketType ticketType) {

        return new TicketTypeResponse(
                ticketType.getId(),
                ticketType.getEvent().getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getTotalQuantity(),
                ticketType.getSoldQuantity(),
                ticketType.getReservedQuantity(),
                ticketType.getAvailableQuantity(),
                ticketType.getStatus()
        );
    }
}
