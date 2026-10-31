package com.johnakins.ticketplatform.ticket;
import com.johnakins.ticketplatform.event.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;
@Entity @Table(name="ticket_types") @Getter @Setter
public class TicketType {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="event_id") private Event event;
 @Column(nullable=false) private String name;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal price;
 @Column(nullable=false) private int totalQuantity;
 @Column(nullable=false) private int soldQuantity=0;
 @Column(nullable=false) private int reservedQuantity=0;

    public int getAvailableQuantity() {
        return totalQuantity - soldQuantity - reservedQuantity;
    }

    public void reserve(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least one"
            );
        }

        if (getAvailableQuantity() < quantity) {
            throw new IllegalStateException(
                    "Insufficient tickets available"
            );
        }

        reservedQuantity += quantity;
    }

    public void confirmReservation(int quantity) {
        if (quantity < 1 || reservedQuantity < quantity) {
            throw new IllegalStateException(
                    "Invalid reservation quantity"
            );
        }

        reservedQuantity -= quantity;
        soldQuantity += quantity;
    }

    public void releaseReservation(int quantity) {
        if (quantity < 1 || reservedQuantity < quantity) {
            throw new IllegalStateException(
                    "Invalid reservation release"
            );
        }

        reservedQuantity -= quantity;
    }

    public TicketTypeStatus getStatus() {

        if (getAvailableQuantity() > 0) {
            return TicketTypeStatus.AVAILABLE;
        }

        if (reservedQuantity > 0) {
            return TicketTypeStatus.UNAVAILABLE;
        }

        return TicketTypeStatus.SOLD_OUT;
    }
}
