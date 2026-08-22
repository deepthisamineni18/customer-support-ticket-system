# Test Cases: Dashboard

| Test Case ID | Test Scenario | Pre-conditions | Test Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_DB_01** | Verify initial dashboard counts from seed data | Seed data loaded in DB | 1. Send `GET /api/tickets/stats`. | Returns accurate counts: `total`, `open`, `inProgress`, `resolved`, `closed`. | PASS |
| **TC_DB_02** | Dashboard stats update after creating a new ticket | Dashboard loaded | 1. Create a new ticket.<br>2. Fetch `GET /api/tickets/stats`. | `total` and `open` counts increment by 1. | PASS |
| **TC_DB_03** | Dashboard stats update after resolving and closing | Ticket in progress | 1. Move ticket to `RESOLVED`, then `CLOSED`.<br>2. Fetch `GET /api/tickets/stats`. | `inProgress` decrements; `closed` increments. | PASS |
| **TC_DB_04** | UI metric card quick filter | UI opened | 1. Click on "Open" metric card in UI. | Filter dropdown changes to `OPEN` and table re-filters to show only open tickets. | PASS |
| **TC_DB_05** | UI Refresh button | UI opened | 1. Click "Refresh" button. | Data is re-fetched asynchronously without page reload. | PASS |
