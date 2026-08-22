# Test Cases: Status Transitions

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_ST_01** | Valid transition: ASSIGNED &rarr; IN_PROGRESS | Ticket is `ASSIGNED` | 1. Send `PUT /api/tickets/{id}/status` with `{ "status": "IN_PROGRESS" }`. | HTTP 200 OK; status updated to `IN_PROGRESS`. | PASS |
| **TC_ST_02** | Valid transition: IN_PROGRESS &rarr; RESOLVED | Ticket is `IN_PROGRESS` | 1. Send `PUT /api/tickets/{id}/resolve` with resolution notes. | HTTP 200 OK; status is `RESOLVED`; notes saved. | PASS |
| **TC_ST_03** | Valid transition: RESOLVED &rarr; CLOSED | Ticket is `RESOLVED` | 1. Send `PUT /api/tickets/{id}/close`. | HTTP 200 OK; status is `CLOSED`. | PASS |
| **TC_ST_04** | Invalid transition: OPEN &rarr; RESOLVED | Ticket is `OPEN` | 1. Send `PUT /api/tickets/{id}/status` with `{ "status": "RESOLVED" }`. | HTTP 400 Bad Request; error indicates invalid status transition. | PASS |
| **TC_ST_05** | Invalid transition: OPEN &rarr; IN_PROGRESS | Ticket is `OPEN` (no agent) | 1. Send `PUT /api/tickets/{id}/status` with `{ "status": "IN_PROGRESS" }`. | HTTP 400 Bad Request; invalid transition. | PASS |
| **TC_ST_06** | Invalid transition: Cannot reopen CLOSED ticket | Ticket is `CLOSED` | 1. Send `PUT /api/tickets/{id}/status` with `{ "status": "OPEN" }`. | HTTP 400 Bad Request; closed tickets cannot be reopened. | PASS |
| **TC_ST_07** | Resolve without resolution notes | Ticket is `IN_PROGRESS` | 1. Send `PUT /api/tickets/{id}/resolve` with empty/blank resolutionNotes. | HTTP 400 Bad Request; resolution notes are mandatory. | PASS |
| **TC_ST_08** | Close ticket before resolving | Ticket is `IN_PROGRESS` | 1. Send `PUT /api/tickets/{id}/close`. | HTTP 400 Bad Request; ticket must be RESOLVED before CLOSED. | PASS |
