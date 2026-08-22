-- ====================================================================
-- Assessment 3: SQL Tasks
-- ====================================================================

-- 1. Tickets by priority (Grouped count of tickets by priority level)
SELECT 
    priority, 
    COUNT(*) AS ticket_count
FROM tickets
GROUP BY priority
ORDER BY ticket_count DESC;


-- 2. Tickets by agent (List each agent with their total assigned tickets)
SELECT 
    a.id AS agent_id,
    a.name AS agent_name,
    a.department,
    COUNT(t.id) AS total_assigned_tickets
FROM agents a
LEFT JOIN tickets t ON a.id = t.agent_id
GROUP BY a.id, a.name, a.department
ORDER BY total_assigned_tickets DESC, a.name ASC;


-- 3. Open tickets (All tickets currently in OPEN status, awaiting assignment)
SELECT 
    t.id,
    t.title,
    t.description,
    t.priority,
    c.name AS customer_name,
    c.email AS customer_email,
    t.created_at
FROM tickets t
JOIN customers c ON t.customer_id = c.id
WHERE t.status = 'OPEN'
ORDER BY t.created_at ASC;


-- 4. Average tickets per agent (Overall average number of tickets assigned across all agents)
SELECT 
    ROUND(CAST(COUNT(t.id) AS DECIMAL(10,2)) / (SELECT COUNT(*) FROM agents), 2) AS avg_tickets_per_agent
FROM tickets t
WHERE t.agent_id IS NOT NULL;


-- 5. Highest-priority unresolved tickets (URGENT and HIGH priority tickets not yet resolved or closed)
SELECT 
    t.id,
    t.title,
    t.priority,
    t.status,
    c.name AS customer_name,
    COALESCE(a.name, 'Unassigned') AS agent_name,
    t.created_at
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
