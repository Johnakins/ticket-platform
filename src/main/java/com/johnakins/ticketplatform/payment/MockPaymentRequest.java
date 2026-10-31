package com.johnakins.ticketplatform.payment;

import java.util.UUID;

public record MockPaymentRequest(
        UUID reservationId,
        MockPaymentOutcome outcome
) {}
