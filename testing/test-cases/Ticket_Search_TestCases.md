# Test Cases: Ticket Search & Filter

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_SR_01** | Search tickets by keyword in title | Tickets exist in DB | 1. Send `GET /api/tickets/search?keyword=Payment`. | Returns tickets containing "Payment" in title. | PASS |
| **TC_SR_02** | Search tickets by keyword in description | Tickets exist in DB | 1. Send `GET /api/tickets/search?keyword=checkout`. | Returns tickets matching "checkout" in description. | PASS |
| **TC_SR_03** | Filter tickets by status | Tickets with various statuses exist | 1. Send `GET /api/tickets/search?status=OPEN`. | Returns only tickets where status is `OPEN`. | PASS |
| **TC_SR_04** | Filter tickets by priority | Tickets with various priorities exist | 1. Send `GET /api/tickets/search?priority=URGENT`. | Returns only tickets where priority is `URGENT`. | PASS |
| **TC_SR_05** | Combined filter (Status + Priority + Keyword) | Tickets exist | 1. Send `GET /api/tickets/search?status=IN_PROGRESS&priority=HIGH&keyword=Password`. | Returns tickets matching all 3 criteria simultaneously. | PASS |
| **TC_SR_06** | Search with no matches | Tickets exist | 1. Send `GET /api/tickets/search?keyword=NonExistentQuery12345`. | Returns empty JSON array `[]`. | PASS |
