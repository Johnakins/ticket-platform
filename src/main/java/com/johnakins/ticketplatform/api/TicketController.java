package com.johnakins.ticketplatform.api;
import com.johnakins.ticketplatform.reservation.TicketReservationRepository;
import com.johnakins.ticketplatform.ticket.*;
import com.johnakins.ticketplatform.verification.QrCodeService;
import jakarta.validation.Valid; import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
 private final TicketService service; private final QrCodeService qr; private final TicketReservationRepository reservation;
 //public TicketController(TicketService service,QrCodeService qr){this.service=service;this.qr=qr;}
 @PostMapping("/purchase") public TicketResponse purchase(@Valid @RequestBody PurchaseRequest r){
  var t=service.reserve(r.ticketTypeId(),r.email(),r.quantity());
  return new TicketResponse(t.getId(),t.getTicketNumber(),t.getQrToken(),qr.asBase64Png(t.getQrToken()),t.getStatus().name(),t.getPurchaserEmail());
 }
 @GetMapping("/{id}") public TicketResponse get(@PathVariable UUID id){
  Ticket t=service.get(id);
  return new TicketResponse(t.getId(),t.getTicketNumber(),t.getQrToken(),qr.asBase64Png(t.getQrToken()),t.getStatus().name(),t.getPurchaserEmail());
 }

 public record PurchaseRequest(@NotNull UUID ticketTypeId,@Email @NotNull String email, int quantity){}
 public record TicketResponse(UUID id,String ticketNumber,String qrToken,String qrCodeBase64,String status,String email){}
}
