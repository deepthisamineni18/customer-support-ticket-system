# Defect / Bug Report Template

## Bug Information

| Field          | Details                                  |
|----------------|------------------------------------------|
| Bug ID         | BUG-001                                  |
| Title          | Short description of the defect          |
| Severity       | Critical / High / Medium / Low           |
| Priority       | P1 / P2 / P3 / P4                        |
| Component      | Backend / Frontend / API / Database      |
| Environment    | Java 17, Spring Boot 3.2.5, MySQL, Chrome|
| Reported By    | QA Tester                                |
| Reported Date  | YYYY-MM-DD                               |
| Status         | Open / In Progress / Fixed / Closed      |

---

## Defect Description

Provide a clear and concise explanation of the issue observed during testing.

---

## Preconditions

- Application is running successfully.
- Required test data is available.
- User is able to access the relevant feature.

---

## Steps to Reproduce

1. Open the application.
2. Navigate to the affected module.
3. Perform the required action.
4. Observe the behavior.

---

## Expected Result

The system should behave according to the business requirements.

---

## Actual Result

Describe the actual behavior observed during testing.

---

## Impact

Describe how the defect affects functionality, user experience, or business workflow.

---

## Test Data Used

Example:

```json
{
  "title": "Payment Failure",
  "priority": "HIGH",
  "customerId": 1
}
```

---

## Error Message / API Response

Example:

```json
{
  "status": 500,
  "error": "Internal Server Error"
}
```

---

## Evidence

### Screenshot

![Bug Screenshot](../screenshots/Bug_001.png)

### Logs

Paste relevant application logs, browser console errors, or stack traces here.

---

## Resolution Details

| Field          | Details          |
|----------------|------------------|
| Fixed By       | Developer Name   |
| Fix Version    | v1.0.1           |
| Retest Status  | PASS / FAIL      |
| Closure Date   | YYYY-MM-DD       |

---

## Final Status

PASS / FAIL / CLOSED