package com.johnakins.ticketplatform.ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TicketRepository extends JpaRepository<Ticket,UUID> {
    List<Ticket> findByReservationId(UUID reservationId);
    //Optional<Ticket> findByTicketNumber(String number);
    //Optional<Ticket> findByQrToken(String token);
}
