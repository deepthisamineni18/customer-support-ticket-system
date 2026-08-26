# Project Assumptions & Design Decisions

---

## 1. Domain & Workflow

### Status Workflow

The ticket lifecycle follows a strict sequential workflow:

```
OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED
```

Only valid status transitions are allowed. A ticket cannot skip workflow stages.

**Valid transitions:**

| Transition | Allowed |
|---|---|
| OPEN → ASSIGNED | ✅ |
| ASSIGNED → IN_PROGRESS | ✅ |
| IN_PROGRESS → RESOLVED | ✅ |
| RESOLVED → CLOSED | ✅ |
| OPEN → RESOLVED | ❌ |
| ASSIGNED → CLOSED | ❌ |

---

### Assignment Rule

- When an `OPEN` ticket is assigned to an agent, its status automatically changes to `ASSIGNED`.
- An `ASSIGNED` ticket may be reassigned to another valid agent before moving to `IN_PROGRESS`.

---

### Resolution Rule

A ticket can be marked as `RESOLVED` only when:

- Current status is `IN_PROGRESS`
- Resolution notes are provided

> Empty or blank resolution notes are not allowed.

---

### Closed Ticket Rule

Once a ticket reaches `CLOSED` status, it becomes **immutable**.

The following operations are not allowed on a closed ticket:

- Reassignment
- Status updates
- Reopening
- Resolution changes

---

### Dashboard Metrics

Dashboard statistics are displayed separately for each status:

| Status | Description |
|---|---|
| `OPEN` | Newly created, unassigned tickets |
| `ASSIGNED` | Tickets assigned to an agent |
| `IN_PROGRESS` | Tickets actively being worked on |
| `RESOLVED` | Tickets resolved, pending closure |
| `CLOSED` | Fully closed tickets |

> The **In Progress** counter represents only tickets with `IN_PROGRESS` status.

---

## 2. Database & Data Model

### Database Choice

- **MySQL** is used as the primary database for the application.
- **Spring Data JPA** and **Hibernate** are used for Object Relational Mapping (ORM) and database interaction.

---

### Primary Keys

All entities use auto-generated `BIGINT` identity keys:

```sql
id BIGINT AUTO_INCREMENT PRIMARY KEY
```

---

### Field Length & Data Type Assumptions

Since exact validation rules were not specified, the following reasonable defaults are assumed for text and enum fields across entities:

| Field | Data Type | Max Length / Constraint | Notes |
|---|---|---|---|
| `title` | VARCHAR | 255 characters (JPA default) | Ticket subject/title |
| `description` | VARCHAR | 2000 characters (`length = 2000`) | Detailed issue description |
| `name` (customer/agent) | VARCHAR | 255 characters (JPA default) | Full name |
| `email` | VARCHAR | 255 characters (JPA default) | Must be unique, valid email format |
| `resolution_notes` | VARCHAR | 2000 characters (`length = 2000`) | Required only when status = `RESOLVED` |
| `status` | VARCHAR (ENUM string) | OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED | Stored as string via `@Enumerated(EnumType.STRING)` |
| `priority` | VARCHAR (ENUM string) | LOW, MEDIUM, HIGH, URGENT | Stored as string via `@Enumerated(EnumType.STRING)` |
| `created_at` / `updated_at` | DATETIME | — | Auto-populated via `@PrePersist` / `@PreUpdate` |