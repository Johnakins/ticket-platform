package com.johnakins.ticketplatform.event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EventRepository extends JpaRepository<Event,UUID> {
 List<Event> findByStatusAndTitleContainingIgnoreCase(EventStatus status,String title);
}
