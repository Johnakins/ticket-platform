package com.johnakins.ticketplatform.verification;
import com.johnakins.ticketplatform.ticket.Ticket;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity @Table(name="ticket_verifications")
public class TicketVerification {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id") private Ticket ticket;
 @Column(nullable=false) private LocalDateTime scannedAt=LocalDateTime.now();
 @Enumerated(EnumType.STRING) @Column(nullable=false) private VerificationResult result;
 private String staffReference;
 protected TicketVerification(){}
 public TicketVerification(Ticket ticket,VerificationResult result,String staffReference){this.ticket=ticket;this.result=result;this.staffReference=staffReference;}
}
