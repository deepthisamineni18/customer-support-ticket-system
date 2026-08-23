# Test Cases: Ticket Search & Filter

## Test Execution Summary

| Total Test Cases | Passed | Failed   | Result |
|------------------|--------|----------|--------|
| 6                | 6      | 0        | PASS   |

---

## Detailed Test Cases

| Test ID  | Test Scenario                         | Status|
|----------|---------------------------------------|-------|
| TC_SR_01 | Search tickets by title keyword       | PASS  |
| TC_SR_02 | Search tickets by description keyword | PASS  |
| TC_SR_03 | Filter tickets by status              | PASS  |
| TC_SR_04 | Filter tickets by priority            | PASS  |
| TC_SR_05 | Search using combined filters         | PASS  |
| TC_SR_06 | Search with no matching records       | PASS  |

---

## Validation Performed

✓ Ticket search by title works correctly

✓ Ticket search by description works correctly

✓ Status filtering returns matching tickets only

✓ Priority filtering returns matching tickets only

✓ Combined filtering works correctly

✓ Empty search results return an empty list

✓ Search API response format is valid

✓ UI search and filter functionality works correctly

---

## Test Result

Search and filtering functionality has been validated successfully. Keyword search, status filtering, priority filtering, and combined search operations return accurate results without errors.

---

## Screenshot Evidence

### Search & Filter Results

![Search Results](../screenshots/UI_Dashboard.png)

### API Search Response

![API Search](../screenshots/API_Dashboard_Stats.png)