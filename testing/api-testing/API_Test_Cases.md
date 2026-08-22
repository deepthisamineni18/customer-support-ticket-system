# API Test Cases: Endpoints Verification

| Endpoint | Method | Test Scenario | Request Body / Params | Expected Status | Response Assertion | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/api/tickets` | `POST` | Create valid ticket | `{ "title": "T1", "description": "D1", "priority": "HIGH", "customerId": 1 }` | `201 Created` | `jsonPath("$.status").value("OPEN")` | PASS |
| `/api/tickets` | `POST` | Missing title | `{ "title": "", "description": "D1", "priority": "HIGH", "customerId": 1 }` | `400 Bad Request` | `jsonPath("$.error").value("Validation Failed")` | PASS |
| `/api/tickets` | `GET` | Retrieve all tickets | None | `200 OK` | `jsonPath("$").isArray()` | PASS |
| `/api/tickets/{id}` | `GET` | Get existing ticket | ID = 1 | `200 OK` | `jsonPath("$.id").value(1)` | PASS |
| `/api/tickets/{id}` | `GET` | Non-existent ticket | ID = 999 | `404 Not Found` | `jsonPath("$.status").value(404)` | PASS |
| `/api/tickets/{id}/assign` | `PUT` | Assign agent | `{ "agentId": 1 }` | `200 OK` | `jsonPath("$.status").value("ASSIGNED")` | PASS |
| `/api/tickets/{id}/status` | `PUT` | Valid status change | `{ "status": "IN_PROGRESS" }` | `200 OK` | `jsonPath("$.status").value("IN_PROGRESS")` | PASS |
| `/api/tickets/{id}/resolve` | `PUT` | Resolve ticket | `{ "resolutionNotes": "Fixed issue" }` | `200 OK` | `jsonPath("$.status").value("RESOLVED")` | PASS |
| `/api/tickets/{id}/close` | `PUT` | Close ticket | None | `200 OK` | `jsonPath("$.status").value("CLOSED")` | PASS |
| `/api/tickets/search` | `GET` | Filter by status | `?status=OPEN` | `200 OK` | All items have `status: "OPEN"` | PASS |
| `/api/tickets/stats` | `GET` | Get dashboard stats | None | `200 OK` | Contains `total, open, inProgress, resolved, closed` | PASS |
| `/api/customers` | `GET` | Get customers list | None | `200 OK` | Returns list of customer objects | PASS |
| `/api/agents` | `GET` | Get agents list | None | `200 OK` | Returns list of agent objects | PASS |
