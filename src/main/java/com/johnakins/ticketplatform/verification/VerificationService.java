package com.johnakins.ticketplatform.verification;
import com.johnakins.ticketplatform.ticket.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class VerificationService {
 private final TicketRepository tickets; private final TicketVerificationRepository logs;
 public VerificationService(TicketRepository tickets,TicketVerificationRepository logs){this.tickets=tickets;this.logs=logs;}
 @Transactional public VerificationResponse verify(String value,String staff){
  Ticket ticket=tickets.findByQrToken(value).or(()->tickets.findByTicketNumber(value)).orElse(null);
  if(ticket==null){logs.save(new TicketVerification(null,VerificationResult.NOT_FOUND,staff));return new VerificationResponse(false,"Ticket not found",null);}
  VerificationResult result;
  if(ticket.getStatus()==TicketStatus.USED) result=VerificationResult.ALREADY_USED;
  else if(ticket.getStatus()==TicketStatus.CANCELLED) result=VerificationResult.CANCELLED;
  else {ticket.markUsed();result=VerificationResult.ACCEPTED;}
  logs.save(new TicketVerification(ticket,result,staff));
  return new VerificationResponse(result==VerificationResult.ACCEPTED,result.name(),ticket.getTicketNumber());
 }
 public record VerificationResponse(boolean accepted,String message,String ticketNumber){}
}
