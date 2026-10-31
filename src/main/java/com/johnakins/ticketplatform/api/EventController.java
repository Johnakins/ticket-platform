package com.johnakins.ticketplatform.api;
import com.johnakins.ticketplatform.event.*;
import com.johnakins.ticketplatform.ticket.*;
import jakarta.validation.Valid; import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.*;
@RestController @RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {
 private final EventService service;
 @GetMapping("/events") public List<Event> search(@RequestParam(defaultValue="") String search){return service.search(search);}
 @GetMapping("/events/{id}") public Event get(@PathVariable UUID id){return service.get(id);}
 @GetMapping("/events/{id}/ticket-types") public List<TicketType> types(@PathVariable UUID id){return service.types(id);}
 @PostMapping("/admin/events") public Event create(@Valid @RequestBody CreateEventRequest r){return service.create(r.title(),r.description(),r.venue(),r.startDate(),r.endDate());}
 @PostMapping("/admin/events/{id}/ticket-types") public TicketType addType(@PathVariable UUID id,@Valid @RequestBody CreateTicketTypeRequest r){return service.addType(id,r.name(),r.price(),r.totalQuantity());}
 @GetMapping("/admin/events/{id}/ticket-types") public TicketType getAllTypes(@PathVariable UUID id){return ;}
 @PatchMapping("/admin/events/{id}/publish") public Event publish(@PathVariable UUID id){return service.publish(id);}
 @PutMapping("/admin/events/{id}") public Event updateEvent(@PathVariable UUID id){return ;}

    public record CreateEventRequest(@NotBlank String title,String description,@NotBlank String venue,@NotNull LocalDateTime startDate,@NotNull LocalDateTime endDate){}
    public record CreateTicketTypeRequest(@NotBlank String name,@NotNull @DecimalMin("0.00") BigDecimal price,@Min(1) int totalQuantity){}
}
