package com.johnakins.ticketplatform.payment;

import com.johnakins.ticketplatform.reservation.TicketReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketReservationService reservationService;

    @Transactional
    public Payment simulatePayment(UUID reservationId, MockPaymentOutcome outcome) {
        Payment payment = paymentRepository
                .findByReservationId(reservationId)
                .orElseGet(() -> createPayment(reservationId));

        // Don't process an already completed payment again.
        if (payment.getStatus() == PaymentStatus.SUCCESS
                || payment.getStatus() == PaymentStatus.FAILED) {
            return payment;
        }

        switch (outcome) {

            case SUCCESS -> {
                reservationService.confirmReservation(reservationId);
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setFailureReason(null);
            }

            case FAILED -> {
                reservationService.releaseReservation(reservationId);
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Mock payment declined");
            }

            case PENDING -> {
                payment.setStatus(PaymentStatus.PENDING);
            }
        }

        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    private Payment createPayment(UUID reservationId) {

        Payment payment = new Payment();

        payment.setReservationId(reservationId);
        payment.setReference("MOCK-" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}
