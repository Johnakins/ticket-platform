package com.johnakins.ticketplatform.api;
import com.johnakins.ticketplatform.verification.VerificationService;
import jakarta.validation.Valid; import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/tickets")
public class VerificationController {
// private final VerificationService service;
// public VerificationController(VerificationService service){this.service=service;}
// @PostMapping("/verify") public VerificationService.VerificationResponse verify(@Valid @RequestBody VerifyRequest r){return service.verify(r.value(),r.staffReference());}
// public record VerifyRequest(@NotBlank String value,String staffReference){}
}
