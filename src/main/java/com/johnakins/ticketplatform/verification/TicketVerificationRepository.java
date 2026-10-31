package com.johnakins.ticketplatform.verification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface TicketVerificationRepository extends JpaRepository<TicketVerification,UUID> {}
