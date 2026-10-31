package com.johnakins.ticketplatform.event;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name="events")
public class Event {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(nullable=false) private String title;
 @Column(length=3000) private String description;
 @Column(nullable=false) private String venue;
 @Column(nullable=false) private LocalDateTime startDate;
 @Column(nullable=false) private LocalDateTime endDate;


 @Enumerated(EnumType.STRING) @Column(nullable=false) private EventStatus status=EventStatus.DRAFT;

 protected Event() {}
 public Event(String title,String description,String venue,LocalDateTime startDate,LocalDateTime endDate) {
  this.title=title; this.description=description; this.venue=venue; this.startDate=startDate; this.endDate=endDate;
 }
 public UUID getId(){return id;} public String getTitle(){return title;} public String getDescription(){return description;}
 public String getVenue(){return venue;} public LocalDateTime getStartDate(){return startDate;} public LocalDateTime getEndDate(){return endDate;}
 public EventStatus getStatus(){return status;} public void publish(){status=EventStatus.PUBLISHED;}
}
