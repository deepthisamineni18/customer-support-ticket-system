# Dashboard Test Cases

## Dashboard Test Execution Summary

| Total Test Cases | Passed | Failed | Status |
|------------------|--------|--------|--------|
| 5                | 5      | 0      | PASS   |

---

## Dashboard Test Cases

| Test ID       | Scenario                            | Status |
|---------------|-------------------------------------|--------|
| TC_DB_01      | Dashboard Statistics Validation     | PASS   |
| TC_DB_02      | Create Ticket Statistics Update     | PASS   |
| TC_DB_03      | Resolve & Close Statistics Update   | PASS   |
| TC_DB_04      | Dashboard Quick Filter              | PASS   |
| TC_DB_05      | Dashboard Refresh Functionality     | PASS   |

---

## Validation Details

### TC_DB_01 – Dashboard Statistics Validation
- Verified dashboard metrics are loaded correctly.
- Total, Open, In Progress, Resolved, and Closed counts match database records.
- Result: PASS

### TC_DB_02 – Create Ticket Statistics Update
- Created a new ticket from UI/API.
- Verified Total Tickets and Open Tickets count increased.
- Result: PASS

### TC_DB_03 – Resolve & Close Statistics Update
- Updated ticket from In Progress → Resolved → Closed.
- Verified dashboard counts updated correctly.
- Result: PASS

### TC_DB_04 – Dashboard Quick Filter
- Selected dashboard status filter.
- Verified ticket table displays matching records only.
- Result: PASS

### TC_DB_05 – Dashboard Refresh Functionality
- Clicked Refresh button.
- Verified latest ticket data loaded without page refresh.
- Result: PASS

---

## Dashboard Testing Evidence

### Dashboard UI

![Dashboard UI](../screenshots/UI_Dashboard.png)

### Dashboard Statistics API

![Dashboard Statistics](../screenshots/API_Dashboard_Stats.png)

---

## Dashboard Testing Result

All dashboard-related functionalities were tested successfully.

- Statistics Validation: PASS
- Ticket Count Updates: PASS
- Quick Filters: PASS
- Refresh Functionality: PASS
- UI & API Synchronization: PASS

**Final Status: PASS**
