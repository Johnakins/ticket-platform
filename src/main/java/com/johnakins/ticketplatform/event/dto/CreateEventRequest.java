package com.johnakins.ticketplatform.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateEventRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @Size(max = 2000)
        String description,

        @NotBlank
        String venue,

        @NotNull
        @Future
        LocalDateTime startDate,

        @NotNull
        @Future
        LocalDateTime endDate

) {}