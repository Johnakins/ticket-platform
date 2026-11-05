package com.johnakins.ticketplatform.verification;
import com.johnakins.ticketplatform.ticket.*;
import com.johnakins.ticketplatform.verification.dto.TicketVerificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final TicketRepository ticketRepository;

    @Transactional
    public TicketVerificationResponse verifyTicket(String qrToken) {

        Ticket ticket = ticketRepository.findByQrTokenForUpdate(qrToken)
                .orElse(null);

        if (ticket == null) {
            return new TicketVerificationResponse(
                    false,
                    "Invalid ticket. QR code not recognised.",
                    null,
                    null,
                    null
            );
        }

        if (ticket.getStatus() == TicketStatus.USED) {
            return new TicketVerificationResponse(
                    false,
                    "Ticket has already been used.",
                    ticket.getId(),
                    ticket.getTicketNumber(),
                    ticket.getStatus()
            );
        }

        if (ticket.getStatus() != TicketStatus.VALID) {
            return new TicketVerificationResponse(
                    false,
                    "Ticket is not valid for entry.",
                    ticket.getId(),
                    ticket.getTicketNumber(),
                    ticket.getStatus()
            );
        }

        // Mark ticket as used after successful verification.
        ticket.setStatus(TicketStatus.USED);

        ticketRepository.save(ticket);

        return new TicketVerificationResponse(
                true,
                "Ticket verified successfully. Entry allowed.",
                ticket.getId(),
                ticket.getTicketNumber(),
                ticket.getStatus()
        );
    }
}
