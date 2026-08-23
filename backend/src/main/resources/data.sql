/* --------------------------------------------------------------
   Customer‑Support Ticket System – seed data
   --------------------------------------------------------------
   This script runs on every Spring Boot start (spring.sql.init.mode=always)
   but each INSERT is guarded by a NOT‑EXISTS check, so the data is
   inserted only the first time the DB is empty.
   -------------------------------------------------------------- */

/* ----------------------------------------------------------------
   1️⃣  Customers  (identified by unique e‑mail)
   ---------------------------------------------------------------- */
INSERT INTO customers (name, email, phone)
SELECT 'Alice Johnson',   'alice.johnson@example.com',   '+1-555-0101'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'alice.johnson@example.com');

INSERT INTO customers (name, email, phone)
SELECT 'Bob Smith',       'bob.smith@example.com',       '+1-555-0102'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'bob.smith@example.com');

INSERT INTO customers (name, email, phone)
SELECT 'Charlie Brown',   'charlie.brown@example.com',   '+1-555-0103'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'charlie.brown@example.com');

INSERT INTO customers (name, email, phone)
SELECT 'Diana Prince',    'diana.prince@example.com',    '+1-555-0104'
WHERE NOT EXISTS (SELECT 1 FROM customers WHERE email = 'diana.prince@example.com');

/* ----------------------------------------------------------------
   2️⃣  Agents  (identified by unique e‑mail)
   ---------------------------------------------------------------- */
INSERT INTO agents (name, email, department)
SELECT 'Sarah Connor',    'sarah.connor@support.com',    'Technical Support'
WHERE NOT EXISTS (SELECT 1 FROM agents WHERE email = 'sarah.connor@support.com');

INSERT INTO agents (name, email, department)
SELECT 'John Miller',     'john.miller@support.com',     'Billing Support'
WHERE NOT EXISTS (SELECT 1 FROM agents WHERE email = 'john.miller@support.com');

INSERT INTO agents (name, email, department)
SELECT 'Emma Watson',     'emma.watson@support.com',     'Customer Success'
WHERE NOT EXISTS (SELECT 1 FROM agents WHERE email = 'emma.watson@support.com');

/* ----------------------------------------------------------------
   3️⃣  Tickets  (identified by unique title – titles of the seed
                tickets are guaranteed to be unique)
   ---------------------------------------------------------------- */

/* 1️⃣ Payment Gateway Timeout */
INSERT INTO tickets (title, description, priority, status,
                     customer_id, agent_id,
                     resolution_notes, created_at, updated_at)
SELECT 'Payment Gateway Timeout',
       'Customer charged twice during checkout',
       'URGENT',
       'ASSIGNED',
       c.id,
       a.id,
       NULL,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM   (SELECT id FROM customers WHERE email = 'alice.johnson@example.com') c,
       (SELECT id FROM agents    WHERE email = 'sarah.connor@support.com') a
WHERE  NOT EXISTS (SELECT 1 FROM tickets WHERE title = 'Payment Gateway Timeout');

/* 2️⃣ Cannot Reset Password */
INSERT INTO tickets (title, description, priority, status,
                     customer_id, agent_id,
                     resolution_notes, created_at, updated_at)
SELECT 'Cannot Reset Password',
       'Password reset email link is expired immediately',
       'HIGH',
       'IN_PROGRESS',
       c.id,
       a.id,
       NULL,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM   (SELECT id FROM customers WHERE email = 'bob.smith@example.com') c,
       (SELECT id FROM agents    WHERE email = 'john.miller@support.com') a
WHERE  NOT EXISTS (SELECT 1 FROM tickets WHERE title = 'Cannot Reset Password');

/* 3️⃣ Feature Request: Export to CSV */
INSERT INTO tickets (title, description, priority, status,
                     customer_id, agent_id,
                     resolution_notes, created_at, updated_at)
SELECT 'Feature Request: Export to CSV',
       'Requesting an option to export order reports to CSV',
       'LOW',
       'OPEN',
       c.id,
       NULL,
       NULL,
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM   (SELECT id FROM customers WHERE email = 'charlie.brown@example.com') c
WHERE  NOT EXISTS (SELECT 1 FROM tickets WHERE title = 'Feature Request: Export to CSV');

/* 4️⃣ App Crashes on Profile Edit */
INSERT INTO tickets (title, description, priority, status,
                     customer_id, agent_id,
                     resolution_notes, created_at, updated_at)
SELECT 'App Crashes on Profile Edit',
       'Mobile app crashes whenever updating phone number',
       'HIGH',
       'RESOLVED',
       c.id,
       a.id,
       'Fixed null pointer exception in profile service patch v1.2',
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM   (SELECT id FROM customers WHERE email = 'diana.prince@example.com') c,
       (SELECT id FROM agents    WHERE email = 'sarah.connor@support.com') a
WHERE  NOT EXISTS (SELECT 1 FROM tickets WHERE title = 'App Crashes on Profile Edit');

/* 5️⃣ Account Onboarding Assistance */
INSERT INTO tickets (title, description, priority, status,
                     customer_id, agent_id,
                     resolution_notes, created_at, updated_at)
SELECT 'Account Onboarding Assistance',
       'Walkthrough required for multi‑user company setup',
       'MEDIUM',
       'CLOSED',
       c.id,
       a.id,
       'Completed onboarding orientation call with team.',
       CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP
FROM   (SELECT id FROM customers WHERE email = 'alice.johnson@example.com') c,
       (SELECT id FROM agents    WHERE email = 'emma.watson@support.com') a
WHERE  NOT EXISTS (SELECT 1 FROM tickets WHERE title = 'Account Onboarding Assistance');

/* ----------------------------------------------------------------
   End of seed data
   ---------------------------------------------------------------- */
