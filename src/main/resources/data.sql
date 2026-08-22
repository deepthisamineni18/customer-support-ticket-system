-- Initial Seed Data (IDs omitted to keep database identity sequences in sync)

-- Customers
INSERT INTO customers (name, email, phone) VALUES ('Alice Johnson', 'alice.johnson@example.com', '+1-555-0101');
INSERT INTO customers (name, email, phone) VALUES ('Bob Smith', 'bob.smith@example.com', '+1-555-0102');
INSERT INTO customers (name, email, phone) VALUES ('Charlie Brown', 'charlie.brown@example.com', '+1-555-0103');
INSERT INTO customers (name, email, phone) VALUES ('Diana Prince', 'diana.prince@example.com', '+1-555-0104');

-- Agents
INSERT INTO agents (name, email, department) VALUES ('Sarah Connor', 'sarah.connor@support.com', 'Technical Support');
INSERT INTO agents (name, email, department) VALUES ('John Miller', 'john.miller@support.com', 'Billing Support');
INSERT INTO agents (name, email, department) VALUES ('Emma Watson', 'emma.watson@support.com', 'Customer Success');

-- Initial Sample Tickets
INSERT INTO tickets (title, description, priority, status, customer_id, agent_id, resolution_notes, created_at, updated_at)
VALUES ('Payment Gateway Timeout', 'Customer charged twice during checkout', 'URGENT', 'ASSIGNED', 1, 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (title, description, priority, status, customer_id, agent_id, resolution_notes, created_at, updated_at)
VALUES ('Cannot Reset Password', 'Password reset email link is expired immediately', 'HIGH', 'IN_PROGRESS', 2, 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (title, description, priority, status, customer_id, agent_id, resolution_notes, created_at, updated_at)
VALUES ('Feature Request: Export to CSV', 'Requesting an option to export order reports to CSV', 'LOW', 'OPEN', 3, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (title, description, priority, status, customer_id, agent_id, resolution_notes, created_at, updated_at)
VALUES ('App Crashes on Profile Edit', 'Mobile app crashes whenever updating phone number', 'HIGH', 'RESOLVED', 4, 1, 'Fixed null pointer exception in profile service patch v1.2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (title, description, priority, status, customer_id, agent_id, resolution_notes, created_at, updated_at)
VALUES ('Account Onboarding Assistance', 'Walkthrough required for multi-user company setup', 'MEDIUM', 'CLOSED', 1, 3, 'Completed onboarding orientation call with team.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
