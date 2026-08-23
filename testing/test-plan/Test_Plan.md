# Test Plan: Customer Support Ticket System

## 1. Introduction & Objectives

This test plan defines the testing approach for the Customer Support Ticket System. The objective is to verify that the application functions correctly across the user interface, REST APIs, business workflows, and database layers.

Testing focuses on validating ticket lifecycle management, business rule enforcement, data integrity, dashboard functionality, and overall system reliability to ensure the application meets the specified requirements and is ready for deployment.

---

## 2. Scope

### In-Scope

The following areas are covered as part of testing:

- Ticket management functionality, including ticket creation, assignment, status updates, resolution, and closure.
- Business rule validation, including valid workflow transitions (`OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`) and prevention of modifications to closed tickets.
- REST API testing, including request validation, response validation, status codes, and error handling.
- Database testing, including data persistence, foreign key relationships, duplicate data handling, and SQL query validation.
- Frontend dashboard testing, including dynamic data loading, dashboard statistics, search, filtering, workflow actions, and toast notifications.
- Automated testing using JUnit 5, Mockito, Spring Boot Test, and MockMvc.

### Out-of-Scope

The following areas are not covered in this testing cycle:

- Performance and load testing.
- Security testing such as authentication and role-based access control.
- Production deployment testing.

---

## 3. Test Types & Methodologies

### 1. Unit Testing

Business logic validation using JUnit 5 and Mockito.

**Test Class:**

- `TicketServiceTest`

**Coverage:**

- Ticket creation validation
- Ticket assignment logic
- Status transition rules
- Resolution and closure validations
- Exception and edge-case handling

### 2. Integration Testing

REST API validation using Spring Boot Test and MockMvc.

**Test Class:**

- `TicketControllerIntegrationTest`

**Coverage:**

- API endpoint validation
- Request and response verification
- HTTP status code validation
- End-to-end service integration
- Error handling scenarios

### 3. Database Testing

Verification of database operations and data integrity using MySQL.

**Coverage:**

- Ticket record creation and updates
- Foreign key relationships
- Data persistence validation
- Duplicate seed data handling
- SQL query validation
- Dashboard statistics validation

### 4. UI & End-to-End Testing

Validation of frontend functionality and complete business workflows.

**Coverage:**

- Dashboard loading
- Ticket creation
- Search functionality
- Status filtering
- Priority filtering
- Clear filters functionality
- Ticket assignment workflow
- Status updates
- Ticket resolution and closure
- Toast notifications
- API and database integration through UI

---

## 4. Test Environment

| Component | Details |
|------------|----------|
| Operating System | Windows 10 / Windows 11 |
| JDK | OpenJDK 17 |
| Build Tool | Apache Maven 3.8+ |
| Backend Framework | Spring Boot 3.2.5 |
| Application Server | Embedded Tomcat |
| Database | MySQL 8.x |
| Test Database | H2 In-Memory Database |
| API Testing Tool | Postman |
| Browser | Google Chrome |
| IDE | Visual Studio Code |

---

## 5. Entry & Exit Criteria

### Entry Criteria

- Application builds successfully using Maven.
- MySQL database is configured and accessible.
- Seed data is loaded successfully.
- REST APIs are available and responding correctly.
- Frontend application is accessible through the browser.
- Required test data is available.

### Exit Criteria

- All automated unit tests pass successfully.
- All integration tests pass successfully.
- Critical API, database, and UI test cases pass.
- Ticket workflow business rules are validated.
- No open critical or high-severity defects remain.
- Dashboard functionality is verified.
- Search and filtering features work as expected.
- Ticket assignment, resolution, and closure workflows function correctly.
- Test results are documented and reviewed.

---

## 6. Assumptions

The following assumptions were made during testing:

- The application is running successfully and accessible through the browser.
- MySQL database is configured correctly and available throughout testing.
- Seed data for customers, agents, and tickets is loaded successfully before execution of test cases.
- Users have access to all application features without authentication restrictions.
- API endpoints are available at `http://localhost:8080/api`.
- Test execution is performed in a local development environment.
- Browser cache and network connectivity do not impact test execution.
- Ticket IDs are generated automatically by the database.
- Dashboard statistics are calculated based on the current records available in the database.
- Only functional testing, API testing, database testing, UI testing, and automated testing are considered within the scope of this assignment.

---

## 7. Test Deliverables

The following artifacts are included as part of the testing deliverables:

- Test Plan (`TEST_PLAN.md`)
- Test Cases (`TEST_CASES.md`)
- API Documentation (`API_DOCUMENTATION.md`)
- SQL Query Solutions (`SQL_TASKS.md`)
- JUnit Unit Test Results (`TicketServiceTest`)
- Integration Test Results (`TicketControllerIntegrationTest`)
- Postman API Validation Results
- UI Testing Evidence (Screenshots)
- Database Validation Results
- Defect Observations and Fix Verification Report

### Test Execution Screenshots & Visual Evidence

- **Unit Test Results**:
![Unit Test Results](../screenshots/Unit_Test_Result.png)

- **Integration Test Results**:
![Integration Test Results](../screenshots/Integration_Test_Result.png)

- **API Create Ticket**:
![API Create Ticket](../screenshots/API_Create_Ticket.png)

- **Dashboard Stats**:
![Dashboard Stats](../screenshots/API_Dashboard_Stats.png)

- **Database Validation**:
![Database Validation](../screenshots/Database_Tickets_Table.png)

- **UI Dashboard**:
![UI Dashboard](../screenshots/UI_Dashboard.png)

- **UI Ticket Creation**:
![UI Ticket Creation](../screenshots/UI_Ticket_Creation.png)

---

## 8. Test Execution Summary

The following testing activities were completed:

- Unit testing executed successfully using JUnit 5 and Mockito.
- Integration testing executed successfully using Spring Boot Test and MockMvc.
- REST APIs validated using Postman.
- Database records verified using MySQL queries.
- Dashboard statistics validated against database records.
- Ticket creation, assignment, status updates, resolution, and closure workflows verified.
- Search, status filter, priority filter, and clear filter functionality tested through the UI.
- Duplicate seed data issue identified, analyzed, fixed, and revalidated.
- End-to-end validation completed from UI → API → Database.

### Test Results Summary

| Test Area          | Status |
|--------------------|--------|
| Unit Testing       | Passed |
| Integration Testing| Passed |
| API Testing        | Passed |
| Database Testing   | Passed |
| UI Testing         | Passed |
| Workflow Validation| Passed |
| Defect Verification| Passed |

### Overall Result

**PASS** – The Customer Support Ticket System meets the functional, API, database, and UI requirements defined within the scope of this assignment.