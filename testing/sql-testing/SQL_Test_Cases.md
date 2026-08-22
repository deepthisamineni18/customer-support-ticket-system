# SQL Tasks Test Cases & Validation

## 1. Tickets by Priority
- **SQL Statement**:
  ```sql
  SELECT priority, COUNT(*) AS ticket_count
  FROM tickets
  GROUP BY priority
  ORDER BY ticket_count DESC;
  ```
- **Validation**: Grouping matches all priority levels present in the database. Counts equal total tickets.
- **Result**: PASS

---

## 2. Tickets by Agent
- **SQL Statement**:
  ```sql
  SELECT 
      a.id AS agent_id,
      a.name AS agent_name,
      a.department,
      COUNT(t.id) AS total_assigned_tickets
  FROM agents a
  LEFT JOIN tickets t ON a.id = t.agent_id
  GROUP BY a.id, a.name, a.department
  ORDER BY total_assigned_tickets DESC, a.name ASC;
  ```
- **Validation**: Uses `LEFT JOIN` so agents with 0 assigned tickets are still displayed with count 0.
- **Result**: PASS

---

## 3. Open Tickets
- **SQL Statement**:
  ```sql
  SELECT t.id, t.title, t.description, t.priority, c.name AS customer_name, c.email AS customer_email, t.created_at
  FROM tickets t
  JOIN customers c ON t.customer_id = c.id
  WHERE t.status = 'OPEN'
  ORDER BY t.created_at ASC;
  ```
- **Validation**: Returns exclusively tickets with `status = 'OPEN'`. Customer details correctly joined.
- **Result**: PASS

---

## 4. Average Tickets per Agent
- **SQL Statement**:
  ```sql
  SELECT ROUND(CAST(COUNT(t.id) AS DECIMAL(10,2)) / (SELECT COUNT(*) FROM agents), 2) AS avg_tickets_per_agent
  FROM tickets t
  WHERE t.agent_id IS NOT NULL;
  ```
- **Validation**: Correctly calculates assigned tickets divided by total agent count, rounded to 2 decimal places.
- **Result**: PASS

---

## 5. Highest-Priority Unresolved Tickets
- **SQL Statement**:
  ```sql
  SELECT t.id, t.title, t.priority, t.status, c.name AS customer_name, COALESCE(a.name, 'Unassigned') AS agent_name, t.created_at
  FROM tickets t
  JOIN customers c ON t.customer_id = c.id
  LEFT JOIN agents a ON t.agent_id = a.id
  WHERE t.status NOT IN ('RESOLVED', 'CLOSED')
  ORDER BY 
      CASE t.priority 
          WHEN 'URGENT' THEN 1 
          WHEN 'HIGH' THEN 2 
          WHEN 'MEDIUM' THEN 3 
          WHEN 'LOW' THEN 4 
          ELSE 5 
      END ASC,
      t.created_at ASC;
  ```
- **Validation**: Excludes resolved/closed tickets, orders `URGENT` first, then `HIGH`, `MEDIUM`, `LOW`.
- **Result**: PASS
