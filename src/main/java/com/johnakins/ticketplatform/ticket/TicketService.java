package com.johnakins.ticketplatform.ticket;
import com.johnakins.ticketplatform.event.*;
import com.johnakins.ticketplatform.reservation.ReservationStatus;
import com.johnakins.ticketplatform.reservation.TicketReservation;
import com.johnakins.ticketplatform.reservation.TicketReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TicketService {
 private final TicketTypeRepository types;
 private final TicketRepository tickets;
 private final TicketReservationRepository reservations;

 @Transactional
 public TicketReservation reserve(UUID typeId,String email,int quantity){
    TicketType type=types.findByIdForUpdate(typeId).orElseThrow(()->new NoSuchElementException("Ticket type not found"));
    if(type.getEvent().getStatus()!=EventStatus.PUBLISHED) throw new IllegalStateException("Event is not published");
    type.reserve(quantity);

    TicketReservation reservation = new TicketReservation();

    reservation.setTicketTypeId(typeId);
    reservation.setCustomerEmail(email);
    reservation.setQuantity(quantity);
    reservation.setStatus(ReservationStatus.PENDING);
    reservation.setCreatedAt(LocalDateTime.now());
    reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));

    return reservations.save(reservation);
 }

 @Transactional(readOnly=true)
 public Ticket get(UUID id){return tickets.findById(id).orElseThrow(()->new NoSuchElementException("Ticket not found"));}
}
