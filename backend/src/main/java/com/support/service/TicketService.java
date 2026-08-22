package com.support.service;

import com.support.dto.CreateTicketRequest;
import com.support.dto.TicketStatsResponse;
import com.support.entity.Agent;
import com.support.entity.Customer;
import com.support.entity.Ticket;
import com.support.entity.TicketPriority;
import com.support.entity.TicketStatus;
import com.support.repository.AgentRepository;
import com.support.repository.CustomerRepository;
import com.support.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;

    public TicketService(TicketRepository ticketRepository,
                         CustomerRepository customerRepository,
                         AgentRepository agentRepository) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
    }

    public Ticket createTicket(CreateTicketRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + request.getCustomerId()));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCustomer(customer);
        ticket.setStatus(TicketStatus.OPEN);

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found with ID: " + id));
    }

    public Ticket assignTicket(Long ticketId, Long agentId) {
        Ticket ticket = getTicketById(ticketId);

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("A closed ticket cannot be modified or assigned.");
        }

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NoSuchElementException("Agent not found with ID: " + agentId));

        ticket.setAgent(agent);
        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.ASSIGNED);
        }

        return ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = getTicketById(ticketId);
        validateTransition(ticket.getStatus(), newStatus);

        if (newStatus == TicketStatus.ASSIGNED && ticket.getAgent() == null) {
            throw new IllegalStateException("Cannot transition to ASSIGNED without an assigned agent.");
        }

        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }

    public Ticket resolveTicket(Long ticketId, String resolutionNotes) {
        Ticket ticket = getTicketById(ticketId);

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("A closed ticket cannot be reopened or resolved.");
        }

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ticket must be IN_PROGRESS before it can be RESOLVED. Current status: " + ticket.getStatus());
        }

        if (resolutionNotes == null || resolutionNotes.trim().isEmpty()) {
            throw new IllegalArgumentException("Resolution notes are required when resolving a ticket.");
        }

        ticket.setResolutionNotes(resolutionNotes.trim());
        ticket.setStatus(TicketStatus.RESOLVED);
        return ticketRepository.save(ticket);
    }

    public Ticket closeTicket(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("Ticket is already CLOSED.");
        }

        if (ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Ticket must be RESOLVED before it can be CLOSED. Current status: " + ticket.getStatus());
        }

        ticket.setStatus(TicketStatus.CLOSED);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> searchTickets(String keyword, TicketStatus status, TicketPriority priority, Long agentId) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return ticketRepository.searchTickets(cleanKeyword, status, priority, agentId);
    }

    @Transactional(readOnly = true)
    public TicketStatsResponse getStats() {
        long total = ticketRepository.count();
        long open = ticketRepository.countByStatus(TicketStatus.OPEN);
        long inProgress = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS) + ticketRepository.countByStatus(TicketStatus.ASSIGNED);
        long resolved = ticketRepository.countByStatus(TicketStatus.RESOLVED);
        long closed = ticketRepository.countByStatus(TicketStatus.CLOSED);

        return new TicketStatsResponse(total, open, inProgress, resolved, closed);
    }

    private void validateTransition(TicketStatus current, TicketStatus next) {
        if (current == TicketStatus.CLOSED) {
            throw new IllegalStateException("A closed ticket cannot be reopened or transitioned.");
        }

        if (current == next) {
            return;
        }

        boolean valid = switch (current) {
            case OPEN -> (next == TicketStatus.ASSIGNED);
            case ASSIGNED -> (next == TicketStatus.IN_PROGRESS);
            case IN_PROGRESS -> (next == TicketStatus.RESOLVED);
            case RESOLVED -> (next == TicketStatus.CLOSED);
            case CLOSED -> false;
        };

        if (!valid) {
            throw new IllegalStateException("Invalid status transition from " + current + " to " + next);
        }
    }
}
