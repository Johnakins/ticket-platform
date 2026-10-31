# Ticket Platform API

A Java 21 / Spring Boot modular-monolith starter for event publishing, ticket purchasing, QR generation and entrance verification.

## Included
- Event creation, publication and public search
- Regular/VIP ticket types with quantity and derived AVAILABLE/SOLD_OUT status
- Ticket purchase with unique ticket number and QR token
- QR PNG returned as Base64 in the ticket response
- Verification by QR token or ticket number; first valid scan marks the ticket USED
- PostgreSQL via Docker Compose

## Intentionally not included yet
Authentication/authorization, real payment gateway, email/SMS notifications, Kafka, and special last-ticket locking.

**Important:** quantity is validated in the service, but this starter does not implement pessimistic/optimistic locking or atomic inventory reservation. Concurrent purchases can oversell. Add database-level concurrency control before production use. Admin/staff routes are currently public and must be secured before deployment.

## Run
Requirements: JDK 21, Maven 3.9+, Docker.

```bash
docker compose up -d
mvn spring-boot:run
```
API listens on `http://localhost:8081`.

## API examples

Create event:
```http
POST /api/admin/events
Content-Type: application/json

{
  "title": "Lagos Tech Conference",
  "description": "Technology and product conference",
  "venue": "Lagos",
  "startDate": "2026-12-10T09:00:00",
  "endDate": "2026-12-10T17:00:00"
}
```

Add a ticket type (replace EVENT_ID):
```http
POST /api/admin/events/EVENT_ID/ticket-types
Content-Type: application/json

{"name":"VIP","price":25000,"totalQuantity":100}
```

Publish:
```http
PATCH /api/admin/events/EVENT_ID/publish
```

Search:
```http
GET /api/events?search=tech
```

Purchase:
```http
POST /api/tickets/purchase
Content-Type: application/json

{"ticketTypeId":"TICKET_TYPE_ID","email":"buyer@example.com"}
```

Verify using returned `qrToken` or `ticketNumber`:
```http
POST /api/tickets/verify
Content-Type: application/json

{"value":"QR_TOKEN_OR_TICKET_NUMBER","staffReference":"gate-1"}
```

## Package layout
- `event`: event lifecycle and ticket-type setup
- `ticket`: purchase and issued-ticket lifecycle
- `verification`: QR generation and entrance validation
- `api`: REST controllers and API exception mapping

This is a backend project. A separate React/Vite frontend can be added on top of these endpoints.
