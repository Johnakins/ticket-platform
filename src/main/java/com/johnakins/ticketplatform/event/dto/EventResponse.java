package com.johnakins.ticketplatform.event.dto;

import com.johnakins.ticketplatform.event.Event;
import com.johnakins.ticketplatform.event.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(

        UUID id,
        String title,
        String description,
        String venue,
        LocalDateTime startDate,
        LocalDateTime endDate,
        EventStatus status,
        LocalDateTime createdAt

) {

    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue(),
                event.getStartDate(),
                event.getEndDate(),
                event.getStatus(),
                event.getCreatedAt()
        );
    }
}