# Test Plan: Customer Support Ticket System

## 1. Introduction & Objectives
The objective of this test plan is to define the testing strategy, test scenarios, coverage areas, and validation criteria for the Customer Support Ticket System to ensure compliance with functional, business, database, API, and UI requirements.

---

## 2. Scope
### In-Scope
- **Functional & Business Rules**: Ticket creation, agent assignment, state transitions (`OPEN` &rarr; `ASSIGNED` &rarr; `IN_PROGRESS` &rarr; `RESOLVED` &rarr; `CLOSED`), resolution notes validation, closed ticket immutability.
- **REST APIs**: Status codes, input validation, error handling, JSON responses.
- **Database & SQL Queries**: Schema integrity, foreign keys, SQL tasks for analytics.
- **Frontend Dashboard**: Dynamic data loading, status counters, filtering, modal workflows, toast notifications.
- **Automated Tests**: JUnit 5 unit and Spring Boot integration test suites.

### Out-of-Scope
- Performance / Load testing at massive scale.
- Multi-factor authentication or RBAC (unless specified in future increments).

---

## 3. Test Types & Methodologies
1. **Unit Testing**: Isolated service layer testing using Mockito to test business logic and edge cases.
2. **Integration Testing**: End-to-end API testing with Spring Boot Test and MockMvc.
3. **Database Testing**: Verifying table constraints, sequences, foreign keys, and SQL query outputs.
4. **UI & End-to-End Testing**: Testing dashboard interactions, dynamic modals, and client-server REST communication.

---

## 4. Test Environment
- **JDK**: OpenJDK 17 / 21
- **Build Tool**: Apache Maven 3.8+
- **Application Server**: Embedded Tomcat (Spring Boot 3.2.5)
- **Database**: H2 (In-Memory) / MySQL / PostgreSQL
- **Browser**: Google Chrome / Edge / Firefox

---

## 5. Entry & Exit Criteria
- **Entry Criteria**: Maven builds successfully and seed data is loaded.
- **Exit Criteria**: 100% of automated unit and integration tests pass; all critical test cases pass; no blocking or critical defects open.
