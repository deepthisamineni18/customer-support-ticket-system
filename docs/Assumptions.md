# Project Assumptions & Design Decisions

## 1. Domain & Workflow

### Status Workflow

The ticket lifecycle follows a strict sequential workflow:

OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED

Only valid status transitions are allowed. A ticket cannot skip workflow stages.

Examples:

- OPEN → ASSIGNED ✓
- ASSIGNED → IN_PROGRESS ✓
- IN_PROGRESS → RESOLVED ✓
- RESOLVED → CLOSED ✓
- OPEN → RESOLVED ✗
- ASSIGNED → CLOSED ✗

### Assignment Rule

When an OPEN ticket is assigned to an agent, its status automatically changes to ASSIGNED.

An ASSIGNED ticket may be reassigned to another valid agent before moving to IN_PROGRESS.

### Resolution Rule

A ticket can be marked as RESOLVED only when:

- Current status is IN_PROGRESS
- Resolution notes are provided

Empty resolution notes are not allowed.

### Closed Ticket Rule

Once a ticket reaches CLOSED status, it becomes immutable.

The following operations are not allowed:

- Reassignment
- Status updates
- Reopening
- Resolution changes

### Dashboard Metrics

Dashboard statistics are displayed separately for each status:

- OPEN
- ASSIGNED
- IN_PROGRESS
- RESOLVED
- CLOSED

The In Progress counter represents only tickets with IN_PROGRESS status.

---

## 2. Database & Data Model

### Database Choice

MySQL is used as the primary database for the application.

Spring Data JPA and Hibernate are used for Object Relational Mapping (ORM) and database interaction.

### Primary Keys

All entities use auto-generated BIGINT identity keys.

Example:

```sql
id BIGINT AUTO_INCREMENT PRIMARY KEY