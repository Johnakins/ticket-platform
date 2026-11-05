# Event Ticketing Platform

A Java Spring Boot ticketing platform for discovering events, reserving tickets, making mock payments, and verifying tickets at the event entrance.

## Features

- Create, update, publish, and search events
- Create ticket types such as Regular and VIP
- Reserve tickets and manage ticket availability
- Simulate payment success or failure
- Generate tickets with unique ticket numbers and QR tokens
- Verify tickets and prevent a ticket from being used more than once
- Use database transactions and pessimistic locking to help prevent overselling and duplicate scans

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven

## How It Works

1. A customer selects an event and ticket type.
2. The customer reserves tickets.
3. A mock payment is processed.
4. After successful payment, tickets are generated. (Else, release reservations on payment failure and timeout).
5. Each ticket receives a unique ticket number and QR token.
6. At the event entrance, staff scan the QR code and the backend verifies the token.

The QR token is stored in the database. The frontend QR-code image and camera scanning functionality are planned.

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/Johnakins/ticketplatform.git
   cd ticketplatform
   ```

2. Create a PostgreSQL database:

   ```sql
   CREATE DATABASE ticketplatform;
   ```

3. Configure your database connection in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ticketplatform
   spring.datasource.username=YOUR_DB_USERNAME
   spring.datasource.password=YOUR_DB_PASSWORD
   ```

4. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```bash
   mvnw.cmd spring-boot:run
   ```

## Project Status

Backend development is in progress. Frontend QR-code generation and scanning, authentication, real payment integration and notifications are planned.

## Author

John Akinola — [GitHub](https://github.com/Johnakins)
