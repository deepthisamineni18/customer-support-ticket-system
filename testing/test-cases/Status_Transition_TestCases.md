# Status Transition Test Cases

## Status Transition Test Execution Summary

| Total Test Cases | Passed | Failed | Status |
|------------------|--------|--------|--------|
| 8                | 8      | 0      | PASS   |

---

## Status Transition Test Cases

| Test ID        | Scenario                          | Status |
|----------------|-----------------------------------|--------|
| TC_ST_01       | ASSIGNED → IN_PROGRESS            | PASS   |
| TC_ST_02       | IN_PROGRESS → RESOLVED            | PASS   |
| TC_ST_03       | RESOLVED → CLOSED                 | PASS   |
| TC_ST_04       | OPEN → RESOLVED (Invalid)         | PASS   |
| TC_ST_05       | OPEN → IN_PROGRESS (Invalid)      | PASS   |
| TC_ST_06       | Reopen CLOSED Ticket (Invalid)    | PASS   |
| TC_ST_07       | Resolve Without Notes             | PASS   |
| TC_ST_08       | Close Before Resolve              | PASS   |

---

## Validation Details

### TC_ST_01 – ASSIGNED → IN_PROGRESS
- Verified valid status transition from ASSIGNED to IN_PROGRESS.
- HTTP 200 response received.
- Result: PASS

### TC_ST_02 – IN_PROGRESS → RESOLVED
- Verified ticket can be resolved only from IN_PROGRESS state.
- Resolution notes saved successfully.
- Result: PASS

### TC_ST_03 – RESOLVED → CLOSED
- Verified resolved ticket can be closed successfully.
- HTTP 200 response received.
- Result: PASS

### TC_ST_04 – OPEN → RESOLVED (Invalid)
- Attempted direct transition from OPEN to RESOLVED.
- System rejected request with validation error.
- Result: PASS

### TC_ST_05 – OPEN → IN_PROGRESS (Invalid)
- Attempted transition without ticket assignment.
- System prevented invalid workflow transition.
- Result: PASS

### TC_ST_06 – Reopen CLOSED Ticket
- Attempted to modify a CLOSED ticket.
- System rejected request.
- Result: PASS

### TC_ST_07 – Resolve Without Notes
- Attempted to resolve ticket without resolution notes.
- Validation error displayed.
- Result: PASS

### TC_ST_08 – Close Before Resolve
- Attempted to close ticket before resolving.
- System prevented invalid action.
- Result: PASS

---

## Business Rule Validation

### Valid Workflow

OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED

### Rules Verified

- Ticket must be assigned before moving to IN_PROGRESS.
- Resolution notes are mandatory for RESOLVED status.
- Ticket must be RESOLVED before it can be CLOSED.
- CLOSED tickets cannot be modified or reopened.
- Invalid workflow transitions are blocked.

---

## Status Transition Testing Result

All workflow transition scenarios were validated successfully.

- Valid Transitions: PASS
- Invalid Transition Handling: PASS
- Resolution Validation: PASS
- Closed Ticket Protection: PASS
- Business Rules Enforcement: PASS

**Final Status: PASS**