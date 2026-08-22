# Test Cases: Ticket Assignment

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_TA_01** | Assign OPEN ticket to valid agent | Ticket in `OPEN` state, Agent ID 1 exists | 1. Send `PUT /api/tickets/{id}/assign` with `{ "agentId": 1 }`. | HTTP 200 OK; ticket status transitions to `ASSIGNED`; agent details attached. | PASS |
| **TC_TA_02** | Reassign ASSIGNED ticket to another agent | Ticket in `ASSIGNED` state, Agent ID 2 exists | 1. Send `PUT /api/tickets/{id}/assign` with `{ "agentId": 2 }`. | HTTP 200 OK; ticket stays `ASSIGNED` with new agent details. | PASS |
| **TC_TA_03** | Assign ticket with non-existent agent ID | Ticket in `OPEN` state, Agent ID 999 does not exist | 1. Send `PUT /api/tickets/{id}/assign` with `{ "agentId": 999 }`. | HTTP 404 Not Found; error message "Agent not found with ID: 999". | PASS |
| **TC_TA_04** | Attempt to assign a CLOSED ticket | Ticket in `CLOSED` state | 1. Send `PUT /api/tickets/{id}/assign` with `{ "agentId": 1 }`. | HTTP 400 Bad Request; error message "A closed ticket cannot be modified or assigned". | PASS |
| **TC_TA_05** | UI assign workflow | Ticket in `OPEN` state | 1. Open Ticket Manage modal.<br>2. Select agent from dropdown.<br>3. Click "Assign Ticket". | Status badge changes to `ASSIGNED`; dashboard counters update. | PASS |
