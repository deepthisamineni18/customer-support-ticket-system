# UI Test Cases: Frontend & Dashboard Verification

| Test ID | Test Scenario | Steps | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC_UI_01** | Dashboard Initial Load | 1. Open `http://localhost:8080/`. | Metrics cards display values fetched from `/api/tickets/stats`. Tickets table shows seed records. | PASS |
| **TC_UI_02** | Create Ticket Modal | 1. Click "+ New Ticket".<br>2. Fill in form.<br>3. Click "Create Ticket". | Modal closes; toast notification appears; table and metrics update without page reload. | PASS |
| **TC_UI_03** | Status Filter Dropdown | 1. Select "OPEN" from status dropdown. | Table dynamically updates to show only tickets with `OPEN` status badge. | PASS |
| **TC_UI_04** | Priority Filter Dropdown | 1. Select "URGENT" from priority dropdown. | Table dynamically filters to show only `URGENT` tickets. | PASS |
| **TC_UI_05** | Keyword Search | 1. Type keyword in search box. | Table updates in real-time to match title/description. | PASS |
| **TC_UI_06** | Ticket Action Modal Workflow | 1. Click "Manage" on an `OPEN` ticket.<br>2. Select agent and assign.<br>3. Progress to `IN_PROGRESS`.<br>4. Enter resolution notes and click Resolve.<br>5. Click Close. | Modal dynamically advances through each workflow stage correctly; final state indicates read-only closed status. | PASS |
| **TC_UI_07** | Clear Filters Button | 1. Apply multiple filters.<br>2. Click "Clear Filters". | Search box and dropdowns reset to default; full ticket list is reloaded. | PASS |
