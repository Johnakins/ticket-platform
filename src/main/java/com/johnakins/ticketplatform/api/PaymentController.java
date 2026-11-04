package com.johnakins.ticketplatform.api;

import com.johnakins.ticketplatform.payment.MockPaymentRequest;
import com.johnakins.ticketplatform.payment.PaymentResponse;
import com.johnakins.ticketplatform.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Simulate payment success, failure, or pending status
    @PostMapping("/mock")
    public ResponseEntity<PaymentResponse> simulatePayment(
            @Valid @RequestBody MockPaymentRequest request) {

        PaymentResponse response =
                paymentService.simulatePayment(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Get payment by ID
//    @GetMapping("/{paymentId}")
//    public ResponseEntity<PaymentResponse> getPaymentById(
//            @PathVariable UUID paymentId) {
//
//        PaymentResponse response =
//                paymentService.getPaymentById(paymentId);
//
//        return ResponseEntity.ok(response);
//    }
//
//    // Get payment by reference
//    @GetMapping("/reference/{reference}")
//    public ResponseEntity<PaymentResponse> getPaymentByReference(
//            @PathVariable String reference) {
//
//        PaymentResponse response =
//                paymentService.getPaymentByReference(reference);
//
//        return ResponseEntity.ok(response);
//    }
}
