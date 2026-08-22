# Test Cases: Ticket Creation

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_TC_01** | Create ticket with valid data | Valid customer exists (ID: 1) | 1. Send `POST /api/tickets` with title, description, priority: "HIGH", customerId: 1.<br>2. Check response status and body. | HTTP 201 Created; status is `OPEN`; agent is `null`; ID is generated. | PASS |
| **TC_TC_02** | Create ticket with missing title | Valid customer exists | 1. Send `POST /api/tickets` with empty title `""`. | HTTP 400 Bad Request; validation error message indicates title is required. | PASS |
| **TC_TC_03** | Create ticket with missing description | Valid customer exists | 1. Send `POST /api/tickets` with blank description. | HTTP 400 Bad Request; validation error returned. | PASS |
| **TC_TC_04** | Create ticket with invalid customer ID | Customer ID 999 does not exist | 1. Send `POST /api/tickets` with customerId: 999. | HTTP 404 Not Found; error message says "Customer not found". | PASS |
| **TC_TC_05** | Create ticket with missing priority | Valid customer exists | 1. Send `POST /api/tickets` without priority field. | HTTP 400 Bad Request; error message returned. | PASS |
| **TC_TC_06** | UI ticket creation form submission | UI loaded in browser | 1. Click "+ New Ticket".<br>2. Fill all fields.<br>3. Click "Create Ticket". | Modal closes; toast shows "Ticket created successfully!"; table updates with new ticket. | PASS |
