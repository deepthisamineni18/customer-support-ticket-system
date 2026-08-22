# Test Cases: Input & Field Validation

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_VAL_01** | Create ticket with missing mandatory fields | Backend running | 1. Send `POST /api/tickets` with `{}`. | HTTP 400 Bad Request; errors list required fields (`title`, `description`, `priority`, `customerId`). | PASS |
| **TC_VAL_02** | Customer email format validation | Backend running | 1. Send `POST /api/customers` with `email: "not-an-email"`. | HTTP 400 Bad Request with invalid email error. | PASS |
| **TC_VAL_03** | Agent email format validation | Backend running | 1. Send `POST /api/agents` with `email: "invalid-agent-email"`. | HTTP 400 Bad Request with email validation error. | PASS |
| **TC_VAL_04** | Assign ticket with null agent ID | Ticket exists | 1. Send `PUT /api/tickets/{id}/assign` with `{}`. | HTTP 400 Bad Request; `agentId is required`. | PASS |
| **TC_VAL_05** | Update status with null status | Ticket exists | 1. Send `PUT /api/tickets/{id}/status` with `{}`. | HTTP 400 Bad Request; `status is required`. | PASS |
| **TC_VAL_06** | Invalid status enum value | Ticket exists | 1. Send `PUT /api/tickets/{id}/status` with `{ "status": "RANDOM_STATUS" }`. | HTTP 400 Bad Request; message deserialization error. | PASS |
