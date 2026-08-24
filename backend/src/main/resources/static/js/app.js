let currentTickets = [];
let availableAgents = [];
let availableCustomers = [];
let searchDebounceTimer = null;

document.addEventListener('DOMContentLoaded', () => {
    loadCustomersAndAgents();
    loadDashboard();
});

async function loadCustomersAndAgents() {
    try {
        const [custRes, agentRes] = await Promise.all([
            fetch('/api/customers'),
            fetch('/api/agents')
        ]);
        if (custRes.ok) {
            availableCustomers = await custRes.json();
            populateCustomerDropdown();
        }
        if (agentRes.ok) {
            availableAgents = await agentRes.json();
        }
    } catch (err) {
        console.error('Failed to load customers or agents:', err);
    }
}

function populateCustomerDropdown() {
    const custSelect = document.getElementById('create-customer');
    if (!custSelect) return;
    custSelect.innerHTML = '<option value="">-- Select Customer --</option>';
    availableCustomers.forEach(c => {
        const opt = document.createElement('option');
        opt.value = c.id;
        opt.textContent = `${c.name} (${c.email})`;
        custSelect.appendChild(opt);
    });
}

async function loadDashboard() {
    await Promise.all([fetchStats(), fetchTickets()]);
}

async function fetchStats() {
    try {
        const res = await fetch('/api/tickets/stats');
        if (res.ok) {
            const stats = await res.json();
            document.getElementById('stat-total').textContent = stats.total ?? 0;
            document.getElementById('stat-open').textContent = stats.open ?? 0;
            const statAssignedEl = document.getElementById('stat-assigned');
            if (statAssignedEl) statAssignedEl.textContent = stats.assigned ?? 0;
            document.getElementById('stat-progress').textContent = stats.inProgress ?? 0;
            document.getElementById('stat-resolved').textContent = stats.resolved ?? 0;
            document.getElementById('stat-closed').textContent = stats.closed ?? 0;
        }
    } catch (err) {
        console.error('Failed to fetch stats:', err);
    }
}

async function fetchTickets() {
    const status = document.getElementById('filter-status').value;
    const priority = document.getElementById('filter-priority').value;
    const keyword = document.getElementById('search-input').value.trim();

    const params = new URLSearchParams();
    if (status) params.append('status', status);
    if (priority) params.append('priority', priority);
    if (keyword) params.append('keyword', keyword);

    const tbody = document.getElementById('tickets-tbody');
    try {
        const res = await fetch(`/api/tickets/search?${params.toString()}`);
        if (res.ok) {
            currentTickets = await res.json();
            renderTicketTable(currentTickets);
        } else {
            tbody.innerHTML = `<tr><td colspan="8" class="text-center py-4">Failed to load tickets</td></tr>`;
        }
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="8" class="text-center py-4">Error loading tickets</td></tr>`;
    }
}

function renderTicketTable(tickets) {
    const tbody = document.getElementById('tickets-tbody');
    document.getElementById('ticket-count').textContent = tickets.length;

    if (tickets.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="text-center py-4" style="color: var(--text-muted);">No tickets found matching current criteria.</td></tr>`;
        return;
    }

    tbody.innerHTML = tickets.map(t => `
        <tr>
            <td><strong>#${t.id}</strong></td>
            <td class="ticket-title-cell">
                <span class="ticket-title">${escapeHtml(t.title)}</span>
                <span class="ticket-desc">${escapeHtml(t.description)}</span>
            </td>
            <td>${escapeHtml(t.customer ? t.customer.name : 'Unknown')}</td>
            <td><span class="badge priority-${t.priority}">${t.priority}</span></td>
            <td><span class="badge status-${t.status}">${formatStatus(t.status)}</span></td>
            <td>${t.agent ? escapeHtml(t.agent.name) : '<span style="color: var(--text-muted); font-style: italic;">Unassigned</span>'}</td>
            <td style="font-size: 12px; color: var(--text-muted);">${formatDate(t.createdAt)}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="openDetailModal(${t.id})">Manage</button>
            </td>
        </tr>
    `).join('');
}

function handleSearch() {
    clearTimeout(searchDebounceTimer);
    searchDebounceTimer = setTimeout(() => {
        fetchTickets();
    }, 250);
}

function handleFilter() {
    fetchTickets();
}

function filterByStatus(status) {
    document.getElementById('filter-status').value = status;
    fetchTickets();
}

function resetFilters() {
    document.getElementById('search-input').value = '';
    document.getElementById('filter-status').value = '';
    document.getElementById('filter-priority').value = '';
    fetchTickets();
}

// Modal management
function openCreateModal() {
    populateCustomerDropdown();
    document.getElementById('create-ticket-form').reset();
    document.getElementById('modal-create').classList.add('active');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

async function handleCreateTicket(e) {
    e.preventDefault();
    const payload = {
        title: document.getElementById('create-title').value.trim(),
        description: document.getElementById('create-description').value.trim(),
        priority: document.getElementById('create-priority').value,
        customerId: parseInt(document.getElementById('create-customer').value)
    };

    try {
        const res = await fetch('/api/tickets', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            showToast('Ticket created successfully!', 'success');
            closeModal('modal-create');
            loadDashboard();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to create ticket', 'error');
        }
    } catch (err) {
        showToast('Network error while creating ticket', 'error');
    }
}

async function openDetailModal(ticketId) {
    try {
        const res = await fetch(`/api/tickets/${ticketId}`);
        if (!res.ok) {
            showToast('Failed to load ticket details', 'error');
            return;
        }

        const ticket = await res.json();

        document.getElementById('detail-id').textContent = ticket.id;
        document.getElementById('detail-title').textContent = ticket.title;

        const statusBadge = document.getElementById('detail-status-badge');
        statusBadge.className = `badge status-${ticket.status}`;
        statusBadge.textContent = formatStatus(ticket.status);

        const priorityBadge = document.getElementById('detail-priority-badge');
        priorityBadge.className = `badge priority-${ticket.priority}`;
        priorityBadge.textContent = ticket.priority;

        document.getElementById('detail-customer').textContent =
            ticket.customer ? `${ticket.customer.name} (${ticket.customer.email})` : 'Unknown';

        document.getElementById('detail-agent').textContent =
            ticket.agent ? `${ticket.agent.name} (${ticket.agent.department})` : 'Not Assigned';

        document.getElementById('detail-created').textContent = formatDate(ticket.createdAt);
        document.getElementById('detail-updated').textContent = formatDate(ticket.updatedAt);
        document.getElementById('detail-description').textContent = ticket.description;

        const resBox = document.getElementById('resolution-notes-box');
        if (ticket.resolutionNotes) {
            document.getElementById('detail-resolution').textContent = ticket.resolutionNotes;
            resBox.classList.remove('hidden');
        } else {
            resBox.classList.add('hidden');
        }

        renderWorkflowControls(ticket);
        document.getElementById('modal-detail').classList.add('active');
    } catch (err) {
        showToast('Error opening ticket details', 'error');
    }
}

function renderWorkflowControls(ticket) {
    const container = document.getElementById('workflow-controls');
    container.innerHTML = '';

    const agentOptions = availableAgents.map(a =>
        `<option value="${a.id}" ${ticket.agent && ticket.agent.id === a.id ? 'selected' : ''}>${a.name} (${a.department})</option>`
    ).join('');

    if (ticket.status === 'OPEN') {
        container.innerHTML = `
            <div class="workflow-action-row">
                <select id="wf-agent-select">
                    <option value="">-- Choose Agent to Assign --</option>
                    ${agentOptions}
                </select>
                <button class="btn btn-primary btn-sm" onclick="performAssign(${ticket.id})">Assign Ticket</button>
            </div>
            <p style="font-size: 12px; color: var(--text-muted); margin-top: 8px;">
                Assigning an agent transitions status from <strong>OPEN</strong> &rarr; <strong>ASSIGNED</strong>.
            </p>
        `;
    } else if (ticket.status === 'ASSIGNED') {
        container.innerHTML = `
            <div style="display: flex; flex-direction: column; gap: 10px;">
                <div class="workflow-action-row">
                    <button class="btn btn-primary btn-sm" onclick="performStatusUpdate(${ticket.id}, 'IN_PROGRESS')">
                        Start Working (Move to IN_PROGRESS)
                    </button>
                </div>
                <div class="workflow-action-row" style="border-top: 1px dashed var(--border-color); padding-top: 10px;">
                    <span style="font-size: 13px; color: var(--text-muted);">Reassign Agent:</span>
                    <select id="wf-agent-select">
                        ${agentOptions}
                    </select>
                    <button class="btn btn-secondary btn-sm" onclick="performAssign(${ticket.id})">Reassign</button>
                </div>
            </div>
        `;
    } else if (ticket.status === 'IN_PROGRESS') {
        container.innerHTML = `
            <div>
                <textarea id="wf-resolution-notes" rows="3" placeholder="Enter resolution notes / solution details (Required to resolve)..."></textarea>
                <button class="btn btn-success btn-sm" onclick="performResolve(${ticket.id})">
                    &#10004; Mark as RESOLVED
                </button>
            </div>
        `;
    } else if (ticket.status === 'RESOLVED') {
        container.innerHTML = `
            <div class="workflow-action-row">
                <button class="btn btn-danger btn-sm" onclick="performClose(${ticket.id})">
                    &#128274; Close Ticket
                </button>
            </div>
            <p style="font-size: 12px; color: var(--text-muted); margin-top: 8px;">
                Closing this ticket is irreversible. Closed tickets cannot be reopened.
            </p>
        `;
    } else if (ticket.status === 'CLOSED') {
        container.innerHTML = `
            <div style="color: var(--text-muted); font-size: 13px;">
                &#128274; This ticket is <strong>CLOSED</strong>. No further modifications or workflow transitions are permitted.
            </div>
        `;
    }
}

async function performAssign(ticketId) {
    const agentSelect = document.getElementById('wf-agent-select');
    const agentId = agentSelect ? agentSelect.value : null;

    if (!agentId) {
        showToast('Please select an agent to assign', 'error');
        return;
    }

    try {
        const res = await fetch(`/api/tickets/${ticketId}/assign`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ agentId: parseInt(agentId) })
        });

        if (res.ok) {
            showToast('Ticket assigned successfully', 'success');
            openDetailModal(ticketId);
            loadDashboard();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to assign ticket', 'error');
        }
    } catch (err) {
        showToast('Error during ticket assignment', 'error');
    }
}

async function performStatusUpdate(ticketId, newStatus) {
    try {
        const res = await fetch(`/api/tickets/${ticketId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });

        if (res.ok) {
            showToast(`Status updated to ${newStatus}`, 'success');
            openDetailModal(ticketId);
            loadDashboard();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to update status', 'error');
        }
    } catch (err) {
        showToast('Error updating status', 'error');
    }
}

async function performResolve(ticketId) {
    const notesInput = document.getElementById('wf-resolution-notes');
    const notes = notesInput ? notesInput.value.trim() : '';

    if (!notes) {
        showToast('Resolution notes are required to resolve a ticket', 'error');
        return;
    }

    try {
        const res = await fetch(`/api/tickets/${ticketId}/resolve`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ resolutionNotes: notes })
        });

        if (res.ok) {
            showToast('Ticket marked as RESOLVED', 'success');
            openDetailModal(ticketId);
            loadDashboard();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to resolve ticket', 'error');
        }
    } catch (err) {
        showToast('Error resolving ticket', 'error');
    }
}

async function performClose(ticketId) {
    if (!confirm('Are you sure you want to close this ticket? Closed tickets cannot be reopened.')) {
        return;
    }

    try {
        const res = await fetch(`/api/tickets/${ticketId}/close`, {
            method: 'PUT'
        });

        if (res.ok) {
            showToast('Ticket successfully CLOSED', 'success');
            openDetailModal(ticketId);
            loadDashboard();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to close ticket', 'error');
        }
    } catch (err) {
        showToast('Error closing ticket', 'error');
    }
}

// Helpers
function formatStatus(status) {
    if (!status) return '';
    return status.replace('_', ' ');
}

function formatDate(isoStr) {
    if (!isoStr) return '-';
    const date = new Date(isoStr);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>"']/g, m => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[m]));
}

function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => {
        toast.remove();
    }, 3500);
}
