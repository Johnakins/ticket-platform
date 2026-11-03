package com.johnakins.ticketplatform.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationRequest(

        @NotNull
        UUID ticketTypeId,

        @NotBlank
        @Email
        String customerEmail,

        @Min(1)
        int quantity

) {}
