package com.johnakins.ticketplatform.reservation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpiryScheduler {
    private final TicketReservationRepository reservations;
    private final TicketReservationService reservationService;

    @Scheduled(fixedDelay = 60000)
    public void expireReservations() {
        List<UUID> expiredIds =
                reservations.findExpiredReservationIds(
                        ReservationStatus.PENDING,
                        LocalDateTime.now(),
                        PageRequest.of(0, 100)
                );

        for (UUID reservationId : expiredIds) {
            try {
//                // Don't expire a reservation that has a payment record.
//                if (paymentRepository
//                        .findByReservationId(reservationId)
//                        .isPresent()) {
//
//                    continue;
//                }
                reservationService.expireReservation(reservationId);

                log.info(
                        "Processed expired reservation: {}",
                        reservationId
                );

            } catch (Exception ex) {
                log.error(
                        "Failed to expire reservation: {}",
                        reservationId,
                        ex
                );
            }
        }
    }
}

