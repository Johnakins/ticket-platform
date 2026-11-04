package com.johnakins.ticketplatform.payment;

import com.johnakins.ticketplatform.reservation.TicketReservationService;
import com.johnakins.ticketplatform.ticket.dto.TicketResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketReservationService reservationService;

    @Transactional
    public PaymentResponse simulatePayment(MockPaymentRequest request) {

        Payment payment = paymentRepository
                .findByReservationId(request.reservationId())
                .orElseGet(() -> createPayment(request.reservationId()));

        List<TicketResponse> tickets = List.of();

        if (request.outcome() == MockPaymentOutcome.SUCCESS) {

            tickets = reservationService.confirmReservation(
                    request.reservationId()
            );

            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setFailureReason(null);

        } else if (request.outcome() == MockPaymentOutcome.FAILED) {

            reservationService.releaseReservation(
                    request.reservationId()
            );

            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Mock payment failed");

        } else if (request.outcome() == MockPaymentOutcome.PENDING) {

            payment.setStatus(PaymentStatus.PENDING);
            payment.setFailureReason(null);
        }

        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResponse.from(savedPayment, tickets);
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

