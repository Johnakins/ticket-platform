package com.johnakins.ticketplatform.ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface TicketRepository extends JpaRepository<Ticket,UUID> {
    List<Ticket> findByReservationId(UUID reservationId);
    boolean existsByTicketNumber(String ticketNumber);
    boolean existsByQrToken(String qrCodeToken);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.qrToken = :qrToken")
    Optional<Ticket> findByQrTokenForUpdate(
            @Param("qrToken") String qrToken
    );
}
