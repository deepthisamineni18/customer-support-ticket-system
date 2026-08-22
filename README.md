# Customer Support Ticket System

A comprehensive Customer Support Ticket Management application built with **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **Maven**, **SQL**, and **Vanilla HTML/CSS/JavaScript**.

---

## 1. Project Structure

```text
customer-support-ticket-system/
│
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/support/
│       │   │   ├── CustomerSupportApplication.java
│       │   │   ├── controller/
│       │   │   │   ├── AgentController.java
│       │   │   │   ├── CustomerController.java
│       │   │   │   ├── GlobalExceptionHandler.java
│       │   │   │   └── TicketController.java
│       │   │   ├── dto/
│       │   │   │   ├── AssignTicketRequest.java
│       │   │   │   ├── CreateTicketRequest.java
│       │   │   │   ├── ResolveTicketRequest.java
│       │   │   │   ├── StatusUpdateRequest.java
│       │   │   │   └── TicketStatsResponse.java
│       │   │   ├── entity/
│       │   │   │   ├── Agent.java
│       │   │   │   ├── Customer.java
│       │   │   │   ├── Ticket.java
│       │   │   │   ├── TicketPriority.java
│       │   │   │   └── TicketStatus.java
│       │   │   ├── repository/
│       │   │   │   ├── AgentRepository.java
│       │   │   │   ├── CustomerRepository.java
│       │   │   │   └── TicketRepository.java
│       │   │   └── service/
│       │   │       └── TicketService.java
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── schema.sql
│       │       ├── data.sql
│       │       ├── queries.sql
│       │       └── static/
│       │           ├── index.html
│       │           ├── css/style.css
│       │           └── js/app.js
│       └── test/
│           └── java/com/support/
│               ├── controller/TicketControllerIntegrationTest.java
│               └── service/TicketServiceTest.java
│
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── app.js
│
├── database/
│   ├── schema.sql
│   ├── data.sql
│   └── queries.sql
│
├── testing/
│   ├── test-plan/
│   │   └── Test_Plan.md
│   │
│   ├── test-cases/
│   │   ├── Ticket_Creation_TestCases.md
│   │   ├── Ticket_Assignment_TestCases.md
│   │   ├── Status_Transition_TestCases.md
│   │   ├── Ticket_Search_TestCases.md
│   │   ├── Dashboard_TestCases.md
│   │   └── Validation_TestCases.md
│   │
│   ├── api-testing/
│   │   └── API_Test_Cases.md
│   │
│   ├── sql-testing/
│   │   └── SQL_Test_Cases.md
│   │
│   ├── ui-testing/
│   │   └── UI_Test_Cases.md
│   │
│   ├── bug-reports/
│   │   └── Bug_Report_Template.md
│   │
│   └── test-summary/
│       └── Test_Execution_Summary.md
│
├── docs/
│   ├── API_Documentation.md
│   └── Assumptions.md
│
├── README.md
└── .gitignore
```

---

## 2. Technology Stack

- **Backend**: Java 17+, Spring Boot 3.2.5 (Spring Web, Spring Data JPA, Bean Validation)
- **Database**: H2 (default in-memory), MySQL / PostgreSQL compatible drivers
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API)
- **Testing**: JUnit 5, Mockito, Spring Boot Test / MockMvc
- **Build Tool**: Maven

---

## 3. How to Run the Application

### 1. Build and Run Backend
```bash
cd backend
mvn clean test
mvn spring-boot:run
```

### 2. Access the Application
- **Frontend Dashboard**: `http://localhost:8080`
- **H2 Database Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:supportdb`
  - Username: `sa`
  - Password: *(leave blank)*

---

## 4. Ticket Workflow & Business Rules

```text
OPEN ──(assign)──> ASSIGNED ──(start)──> IN_PROGRESS ──(resolve + notes)──> RESOLVED ──(close)──> CLOSED
```

1. **Sequential Workflow**: Only valid transitions permitted (`OPEN` &rarr; `ASSIGNED` &rarr; `IN_PROGRESS` &rarr; `RESOLVED` &rarr; `CLOSED`).
2. **Assignment**: Assigning an agent transitions status from `OPEN` to `ASSIGNED` and validates that the agent exists.
3. **Resolution**: Marked as `RESOLVED` only from `IN_PROGRESS` with mandatory resolution notes.
4. **Closed Ticket Immutability**: Once `CLOSED`, a ticket cannot be modified, reassigned, or reopened.

---

## 5. REST APIs Summary

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/tickets` | Create a new ticket (Status: `OPEN`) | `201 Created` |
| `GET` | `/api/tickets` | Get all tickets | `200 OK` |
| `GET` | `/api/tickets/{id}` | Get ticket by ID | `200 OK` |
| `PUT` | `/api/tickets/{id}/assign` | Assign agent (`OPEN` &rarr; `ASSIGNED`) | `200 OK` |
| `PUT` | `/api/tickets/{id}/status` | Update ticket status | `200 OK` |
| `PUT` | `/api/tickets/{id}/resolve` | Resolve ticket with notes (`IN_PROGRESS` &rarr; `RESOLVED`) | `200 OK` |
| `PUT` | `/api/tickets/{id}/close` | Close ticket (`RESOLVED` &rarr; `CLOSED`) | `200 OK` |
| `GET` | `/api/tickets/search` | Search & filter by keyword, status, priority, agent | `200 OK` |
| `GET` | `/api/tickets/stats` | Dashboard count metrics | `200 OK` |
| `GET` | `/api/customers` | List customers for dropdowns | `200 OK` |
| `GET` | `/api/agents` | List agents for assignment | `200 OK` |

---

## 6. SQL Tasks (`database/queries.sql`)

1. **Tickets by Priority**:
   ```sql
   SELECT priority, COUNT(*) AS ticket_count FROM tickets GROUP BY priority ORDER BY ticket_count DESC;
   ```
2. **Tickets by Agent**:
   ```sql
   SELECT a.id, a.name, a.department, COUNT(t.id) AS total_assigned_tickets
   FROM agents a LEFT JOIN tickets t ON a.id = t.agent_id
   GROUP BY a.id, a.name, a.department ORDER BY total_assigned_tickets DESC;
   ```
3. **Open Tickets**:
   ```sql
   SELECT t.id, t.title, t.priority, c.name AS customer_name, t.created_at
   FROM tickets t JOIN customers c ON t.customer_id = c.id
   WHERE t.status = 'OPEN' ORDER BY t.created_at ASC;
   ```
4. **Average Tickets per Agent**:
   ```sql
   SELECT ROUND(CAST(COUNT(t.id) AS DECIMAL(10,2)) / (SELECT COUNT(*) FROM agents), 2) AS avg_tickets_per_agent
   FROM tickets t WHERE t.agent_id IS NOT NULL;
   ```
5. **Highest-Priority Unresolved Tickets**:
   ```sql
   SELECT t.id, t.title, t.priority, t.status, c.name AS customer_name, COALESCE(a.name, 'Unassigned') AS agent_name
   FROM tickets t JOIN customers c ON t.customer_id = c.id LEFT JOIN agents a ON t.agent_id = a.id
   WHERE t.status NOT IN ('RESOLVED', 'CLOSED')
   ORDER BY CASE t.priority WHEN 'URGENT' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MEDIUM' THEN 3 WHEN 'LOW' THEN 4 END ASC;
   ```
