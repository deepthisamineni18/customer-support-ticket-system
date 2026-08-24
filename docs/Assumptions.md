# Project Assumptions & Design Decisions

## 1. Domain & Workflow
- **Status Workflow**: Strict sequential progression: `OPEN` &rarr; `ASSIGNED` &rarr; `IN_PROGRESS` &rarr; `RESOLVED` &rarr; `CLOSED`.
- **Assignment**: When a ticket in `OPEN` state is assigned to an agent, its status automatically transitions to `ASSIGNED`. An `ASSIGNED` ticket can also be reassigned to another agent before moving to `IN_PROGRESS`.
- **Resolution**: Tickets can only be marked as `RESOLVED` if they are currently `IN_PROGRESS` and non-empty `resolutionNotes` are provided.
- **Closed Ticket Rule**: Once a ticket reaches `CLOSED`, it is permanently locked. No further updates, status changes, or assignments are allowed.
- **Dashboard In-Progress Metric**: For the dashboard counter, `In Progress` represents tickets with `IN_PROGRESS` status only.

## 2. Database & Data Model
- **Primary Keys**: Auto-incrementing `BIGINT` identity IDs for portable compatibility across H2, PostgreSQL, and MySQL.
- **Default In-Memory Database**: H2 is enabled by default to allow instant execution without manual external database setup. MySQL and PostgreSQL configurations are pre-prepared in `application.properties`.
- **Cascades & Lookups**: Tickets reference Customers and Agents via foreign keys. Eager fetching is used for clean JSON serialization of customer and agent display details.

## 3. UI & API Integration
- **Vanilla JS with Fetch API**: Zero heavy client frameworks to ensure clean, fast, dependency-free frontend operation.
- **Dynamic Frontend**: The dashboard metrics, filters, modal forms, and workflow actions communicate exclusively with backend REST endpoints using JSON payloads.
