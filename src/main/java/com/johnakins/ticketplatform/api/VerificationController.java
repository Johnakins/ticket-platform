package com.johnakins.ticketplatform.api;
import com.johnakins.ticketplatform.ticket.TicketService;
import com.johnakins.ticketplatform.verification.VerificationService;
import com.johnakins.ticketplatform.verification.dto.TicketVerificationRequest;
import com.johnakins.ticketplatform.verification.dto.TicketVerificationResponse;
import jakarta.validation.Valid; import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class VerificationController {
    private final TicketService ticketService;

    @PostMapping("/verify")
    public ResponseEntity<TicketVerificationResponse> verifyTicket(
            @RequestBody TicketVerificationRequest request
    ) {
        TicketVerificationResponse response =
                ticketService.verifyTicket(request.qrToken());

        return ResponseEntity.ok(response);
    }
}

