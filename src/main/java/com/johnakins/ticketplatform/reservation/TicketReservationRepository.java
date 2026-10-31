package com.johnakins.ticketplatform.reservation;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;

//import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  TicketReservationRepository extends JpaRepository<TicketReservation, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM TicketReservation r WHERE r.id = :id")
    Optional<TicketReservation> findByIdForUpdate(
            @Param("id") UUID id
    );

    @Query("""
        SELECT r.id
        FROM TicketReservation r
        WHERE r.status = :status
        AND r.expiresAt <= :now
        ORDER BY r.expiresAt ASC
    """)
    List<UUID> findExpiredReservationIds(
            @Param("status") ReservationStatus status,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );
}
