package com.johnakins.ticketplatform.event;
import com.johnakins.ticketplatform.ticket.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
@Service
@RequiredArgsConstructor
public class EventService {
 private final EventRepository events;
 private final TicketTypeRepository types;

 //public EventService(EventRepository events,TicketTypeRepository types){this.events=events;this.types=types;}
 @Transactional public Event create(String title,String description,String venue,LocalDateTime start,LocalDateTime end){
  if(!end.isAfter(start)) throw new IllegalArgumentException("Event end must be after start");
  return events.save(new Event(title,description,venue,start,end));
 }

 @Transactional public TicketType addType(UUID eventId,String name,BigDecimal price,int quantity){
  Event event=events.findById(eventId).orElseThrow(()->new NoSuchElementException("Event not found"));
  if(price==null||price.signum()<0||quantity<1) throw new IllegalArgumentException("Price must be non-negative and quantity must be positive");
  TicketType ticketType = new TicketType();
  ticketType.setEvent(event);
  ticketType.setName(name);
  ticketType.setPrice(price);
  ticketType.setTotalQuantity(quantity);
  ticketType.setSoldQuantity(0);
  ticketType.setReservedQuantity(0);

  return types.save(ticketType);
 }

 @Transactional public Event publish(UUID id){Event e=events.findById(id).orElseThrow(()->new NoSuchElementException("Event not found"));e.publish();return e;}
 @Transactional(readOnly=true) public List<Event> search(String q){return events.findByStatusAndTitleContainingIgnoreCase(EventStatus.PUBLISHED,q==null?"":q);}
 @Transactional(readOnly=true) public Event get(UUID id){return events.findById(id).orElseThrow(()->new NoSuchElementException("Event not found"));}
 @Transactional(readOnly=true) public List<TicketType> types(UUID eventId){return types.findByEventId(eventId);}
}
