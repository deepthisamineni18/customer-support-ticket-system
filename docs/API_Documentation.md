# REST API Documentation

## Base URL
`http://localhost:8080/api`

---

## Endpoints

### 1. Create Ticket
- **Method**: `POST`
- **Path**: `/api/tickets`
- **Request Body**:
```json
{
  "title": "Payment gateway failure",
  "description": "User charged twice on checkout",
  "priority": "HIGH",
  "customerId": 1
}
```
- **Response (201 Created)**:
```json
{
  "id": 1,
  "title": "Payment gateway failure",
  "description": "User charged twice on checkout",
  "priority": "HIGH",
  "status": "OPEN",
  "customer": {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice.johnson@example.com",
    "phone": "+1-555-0101"
  },
  "agent": null,
  "resolutionNotes": null,
  "createdAt": "2026-08-22T20:00:00",
  "updatedAt": "2026-08-22T20:00:00"
}
```
- **Execution Evidence**:
![API Create Ticket](../testing/screenshots/API_Create_Ticket.png)

---

### 2. List All Tickets
- **Method**: `GET`
- **Path**: `/api/tickets`
- **Response (200 OK)**: Array of Ticket objects.

---

### 3. Get Ticket by ID
- **Method**: `GET`
- **Path**: `/api/tickets/{id}`
- **Response (200 OK)**: Ticket object.
- **Error (404 Not Found)**: When ID does not exist.

---

### 4. Assign Ticket to Agent
- **Method**: `PUT`
- **Path**: `/api/tickets/{id}/assign`
- **Request Body**:
```json
{
  "agentId": 2
}
```
- **Response (200 OK)**: Updated ticket with `status: "ASSIGNED"` and agent details.

---

### 5. Update Ticket Status
- **Method**: `PUT`
- **Path**: `/api/tickets/{id}/status`
- **Request Body**:
```json
{
  "status": "IN_PROGRESS"
}
```
- **Response (200 OK)**: Updated ticket.
- **Error (400 Bad Request)**: When transition is illegal (e.g., `OPEN` $\rightarrow$ `RESOLVED` or modifying a `CLOSED` ticket).

---

### 6. Resolve Ticket
- **Method**: `PUT`
- **Path**: `/api/tickets/{id}/resolve`
- **Request Body**:
```json
{
  "resolutionNotes": "Re-routed webhook and refunded the duplicate transaction."
}
```
- **Response (200 OK)**: Ticket with `status: "RESOLVED"` and resolution notes.

---

### 7. Close Ticket
- **Method**: `PUT`
- **Path**: `/api/tickets/{id}/close`
- **Response (200 OK)**: Ticket with `status: "CLOSED"`.
- **Note**: A closed ticket cannot be reopened.

---

### 8. Search & Filter Tickets
- **Method**: `GET`
- **Path**: `/api/tickets/search`
- **Query Parameters**:
  - `keyword` (optional): Searches in `title` and `description`
  - `status` (optional): `OPEN`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`
  - `priority` (optional): `LOW`, `MEDIUM`, `HIGH`, `URGENT`
  - `agentId` (optional): Filter by assigned agent ID
- **Response (200 OK)**: Filtered array of tickets.

---

### 9. Dashboard Statistics
- **Method**: `GET`
- **Path**: `/api/tickets/stats`
- **Response (200 OK)**:
```json
{
  "total": 5,
  "open": 1,
  "inProgress": 2,
  "resolved": 1,
  "closed": 1
}
```
- **Execution Evidence**:
![Dashboard Stats](../testing/screenshots/API_Dashboard_Stats.png)

---

### 10. Customers & Agents Helper APIs
- `GET /api/customers` — List all customers for dropdowns.
- `GET /api/agents` — List all agents for assignment selectors.
