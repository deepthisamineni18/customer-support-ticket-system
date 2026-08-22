# Customer Support Ticket System

A lightweight, robust Customer Support Ticket Management application built with **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **H2/MySQL/PostgreSQL**, and **Vanilla HTML/CSS/JavaScript**.

---

## 1. Features & Capabilities

- **Ticket Lifecycle Workflow**:
  `OPEN` &rarr; `ASSIGNED` &rarr; `IN_PROGRESS` &rarr; `RESOLVED` &rarr; `CLOSED`
- **Business Rule Enforcement**:
  - Only valid sequential transitions are permitted.
  - Closed tickets cannot be modified, reassigned, or reopened.
  - Mandatory fields and email validations are enforced.
  - Ticket assignment validates agent existence in database.
  - Resolving a ticket requires resolution notes.
- **Interactive UI Dashboard**:
  - Live metric counters for Total, Open, In Progress, Resolved, and Closed tickets.
  - Real-time search by title/description and filtering by status and priority.
  - Modal workflow for creating, assigning, progressing, resolving, and closing tickets.
  - All dynamic data is fetched asynchronously via `fetch()` API.
- **SQL Tasks Included**:
  - Complete SQL scripts for priority breakdowns, agent allocations, unresolved bottlenecks, and average tickets per agent.

---

## 2. Project Structure

```text
customer-support/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/support/
    │   │   ├── CustomerSupportApplication.java
    │   │   ├── controller/
    │   │   │   ├── AgentController.java
    │   │   │   ├── CustomerController.java
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── TicketController.java
    │   │   ├── dto/
    │   │   │   ├── AssignTicketRequest.java
    │   │   │   ├── CreateTicketRequest.java
    │   │   │   ├── ResolveTicketRequest.java
    │   │   │   ├── StatusUpdateRequest.java
    │   │   │   └── TicketStatsResponse.java
    │   │   ├── entity/
    │   │   │   ├── Agent.java
    │   │   │   ├── Customer.java
    │   │   │   ├── Ticket.java
    │   │   │   ├── TicketPriority.java
    │   │   │   └── TicketStatus.java
    │   │   ├── repository/
    │   │   │   ├── AgentRepository.java
    │   │   │   ├── CustomerRepository.java
    │   │   │   └── TicketRepository.java
    │   │   └── service/
    │   │       └── TicketService.java
    │   └── resources/
    │       ├── application.properties
    │       ├── schema.sql
    │       ├── data.sql
    │       ├── queries.sql
    │       └── static/
    │           ├── index.html
    │           ├── css/
    │           │   └── styles.css
    │           └── js/
    │               └── app.js
    └── test/
        └── java/com/support/
            ├── controller/
            │   └── TicketControllerIntegrationTest.java
            └── service/
                └── TicketServiceTest.java
```

---

## 3. Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.5 (Spring Web, Spring Data JPA, Bean Validation)
- **Database**: H2 (default in-memory), MySQL / PostgreSQL compatible drivers included
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API)
- **Testing**: JUnit 5, Mockito, Spring Boot Test / MockMvc
- **Build Tool**: Maven

---

## 4. How to Run the Application

### Prerequisites
- Java 17+
- Maven 3.8+

### Step 1: Run Unit & Integration Tests
```bash
mvn clean test
```

### Step 2: Start the Application
```bash
mvn spring-boot:run
```

### Step 3: Access the Application
- **Frontend Dashboard**: Open your browser and navigate to:
  ```text
  http://localhost:8080
  ```
- **H2 Database Web Console**:
  - URL: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:supportdb`
  - User Name: `sa`
  - Password: *(leave blank)*

---

## 5. REST API Documentation

### Tickets API (`/api/tickets`)

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/tickets` | Create a new ticket (Status: `OPEN`) | `201 Created` |
| `GET` | `/api/tickets` | Retrieve all tickets | `200 OK` |
| `GET` | `/api/tickets/{id}` | Get ticket details by ID | `200 OK` |
| `PUT` | `/api/tickets/{id}/assign` | Assign agent (`OPEN` &rarr; `ASSIGNED`) | `200 OK` |
| `PUT` | `/api/tickets/{id}/status` | Update ticket status | `200 OK` |
| `PUT` | `/api/tickets/{id}/resolve` | Resolve ticket with notes (`IN_PROGRESS` &rarr; `RESOLVED`) | `200 OK` |
| `PUT` | `/api/tickets/{id}/close` | Close ticket (`RESOLVED` &rarr; `CLOSED`) | `200 OK` |
| `GET` | `/api/tickets/search` | Search & filter by keyword, status, priority, agent | `200 OK` |
| `GET` | `/api/tickets/stats` | Dashboard metrics (total, open, inProgress, resolved, closed) | `200 OK` |

### Sample JSON Payloads

#### Create Ticket (`POST /api/tickets`)
```json
{
  "title": "Cannot process credit card payment",
  "description": "Payment gateway returns timeout error code 504 on checkout.",
  "priority": "HIGH",
  "customerId": 1
}
```

#### Assign Ticket (`PUT /api/tickets/1/assign`)
```json
{
  "agentId": 2
}
```

#### Update Status (`PUT /api/tickets/1/status`)
```json
{
  "status": "IN_PROGRESS"
}
```

#### Resolve Ticket (`PUT /api/tickets/1/resolve`)
```json
{
  "resolutionNotes": "Updated timeout threshold on payment gateway client to 15 seconds."
}
```

---

## 6. SQL Tasks & Queries (`src/main/resources/queries.sql`)

1. **Tickets by Priority**:
   ```sql
   SELECT priority, COUNT(*) AS ticket_count
   FROM tickets
   GROUP BY priority
   ORDER BY ticket_count DESC;
   ```
2. **Tickets by Agent**:
   ```sql
   SELECT a.id, a.name, a.department, COUNT(t.id) AS total_assigned_tickets
   FROM agents a
   LEFT JOIN tickets t ON a.id = t.agent_id
   GROUP BY a.id, a.name, a.department
   ORDER BY total_assigned_tickets DESC;
   ```
3. **Open Tickets**:
   ```sql
   SELECT t.id, t.title, t.priority, c.name AS customer_name, t.created_at
   FROM tickets t
   JOIN customers c ON t.customer_id = c.id
   WHERE t.status = 'OPEN'
   ORDER BY t.created_at ASC;
   ```
4. **Average Tickets per Agent**:
   ```sql
   SELECT ROUND(CAST(COUNT(t.id) AS DECIMAL(10,2)) / (SELECT COUNT(*) FROM agents), 2) AS avg_tickets_per_agent
   FROM tickets t
   WHERE t.agent_id IS NOT NULL;
   ```
5. **Highest-Priority Unresolved Tickets**:
   ```sql
   SELECT t.id, t.title, t.priority, t.status, c.name AS customer_name, a.name AS agent_name
   FROM tickets t
   JOIN customers c ON t.customer_id = c.id
   LEFT JOIN agents a ON t.agent_id = a.id
   WHERE t.status NOT IN ('RESOLVED', 'CLOSED')
   ORDER BY CASE t.priority WHEN 'URGENT' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MEDIUM' THEN 3 WHEN 'LOW' THEN 4 END ASC;
   ```

---

## 7. Testing Scenarios Covered

1. **Positive Scenarios**:
   - Creating a ticket with valid inputs (default status `OPEN`).
   - Assigning a valid agent transitions status from `OPEN` to `ASSIGNED`.
   - Progressing through the full lifecycle (`OPEN` &rarr; `ASSIGNED` &rarr; `IN_PROGRESS` &rarr; `RESOLVED` &rarr; `CLOSED`).
   - Retrieving accurate dashboard summary statistics.
2. **Negative & Validation Scenarios**:
   - Creating a ticket with missing title, description, priority, or non-existent customer returns `400 Bad Request`.
   - Assigning a non-existent agent returns `404 Not Found`.
   - Attempting invalid transitions (e.g. `OPEN` &rarr; `RESOLVED` or `CLOSED` &rarr; `OPEN`) throws `400 Bad Request`.
   - Attempting to resolve without resolution notes returns `400 Bad Request`.
   - Modifying a `CLOSED` ticket is blocked.
