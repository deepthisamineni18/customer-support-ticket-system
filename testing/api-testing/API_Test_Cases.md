# API Test Cases

## API Test Execution Summary

| Total APIs Tested | Passed | Failed | Status |
|-------------------|--------|--------|--------|
| 12                | 12     | 0      | PASS   |

---

## API Test Cases

| Test ID | API Endpoint | Method | Test Scenario | Expected Result | Status |
|----------|-------------|----------|---------------|-----------------|---------|
| TC_API_01 | /api/tickets | POST | Create a new ticket with valid data | Ticket created successfully with status OPEN | PASS |
| TC_API_02 | /api/tickets | POST | Create ticket with missing mandatory fields | Validation error returned with HTTP 400 | PASS |
| TC_API_03 | /api/tickets | GET | Retrieve all tickets | Complete list of tickets returned | PASS |
| TC_API_04 | /api/tickets/{id} | GET | Retrieve existing ticket by ID | Correct ticket details returned | PASS |
| TC_API_05 | /api/tickets/{id} | GET | Retrieve non-existing ticket | HTTP 404 Not Found returned | PASS |
| TC_API_06 | /api/tickets/{id}/assign | PUT | Assign ticket to an agent | Ticket assigned and status changed to ASSIGNED | PASS |
| TC_API_07 | /api/tickets/{id}/status | PUT | Update ticket status | Status updated successfully | PASS |
| TC_API_08 | /api/tickets/{id}/resolve | PUT | Resolve ticket with resolution notes | Ticket status changed to RESOLVED | PASS |
| TC_API_09 | /api/tickets/{id}/close | PUT | Close resolved ticket | Ticket status changed to CLOSED | PASS |
| TC_API_10 | /api/tickets/search | GET | Search tickets using filters | Matching ticket records returned | PASS |
| TC_API_11 | /api/tickets/stats | GET | Retrieve dashboard statistics | Dashboard metrics returned correctly | PASS |
| TC_API_12 | /api/customers & /api/agents | GET | Retrieve customer and agent lists | Lookup data returned successfully | PASS |

---

## API Validation Coverage

### Ticket Management APIs
- Ticket Creation
- Ticket Retrieval
- Ticket Assignment
- Ticket Status Update
- Ticket Resolution
- Ticket Closure

### Search & Reporting APIs
- Ticket Search
- Status Filtering
- Priority Filtering
- Dashboard Statistics

### Lookup APIs
- Customer List
- Agent List

---

## API Testing Evidence

### Create Ticket API Validation

![API Create Ticket](../screenshots/API_Create_Ticket.png)

### Dashboard Statistics API Validation

![Dashboard Statistics](../screenshots/API_Dashboard_Stats.png)

---

## API Testing Result

All API endpoints were tested successfully using Postman and automated integration tests.

- Total APIs Tested: 12
- Passed: 12
- Failed: 0
- HTTP Status Codes Verified
- Request & Response Validation Completed
- Error Handling Verified
- Business Rules Validated

**Final Status: PASS**