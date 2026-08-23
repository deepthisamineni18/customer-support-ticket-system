# Test Cases: Input & Field Validation

## Test Execution Summary

| Total Test Cases | Passed | Failed   | Result |
|------------------|--------|----------|--------|
| 6                | 6      | 0        | PASS   |

---

## Detailed Test Cases

| Test ID  | Test Scenario                              | Status|
|----------|--------------------------------------------|-------|
| TC_VAL_01| Create ticket with missing mandatory fields| PASS  |
| TC_VAL_02| Customer email format validation           | PASS  |
| TC_VAL_03| Agent email format validation              | PASS  |
| TC_VAL_04| Assign ticket with null agent ID           | PASS  |
| TC_VAL_05| Update status with null status             | PASS  |
| TC_VAL_06| Invalid status enum value                  | PASS  |

---

## Validation Performed

✓ Mandatory field validation is working correctly

✓ Customer email format validation is verified

✓ Agent email format validation is verified

✓ Agent assignment request validation is verified

✓ Status update request validation is verified

✓ Invalid enum values are rejected correctly

✓ HTTP 400 Bad Request responses are returned for invalid inputs

✓ Error messages are displayed correctly for validation failures

---

## Test Result

Input validation and field-level validation have been tested successfully. Required fields, email formats, request payload validation, and invalid enum handling behave according to business and API requirements.

---

## Screenshot Evidence

### API Validation Response

![API Validation](../screenshots/API_Create_Ticket.png)

### UI Validation Message

![UI Validation](../screenshots/UI_Ticket_Creation.png)