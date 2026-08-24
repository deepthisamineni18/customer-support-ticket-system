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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private TicketService ticketService;

    private Customer sampleCustomer;
    private Agent sampleAgent;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer(1L, "Alice Johnson", "alice@example.com", "+1-555-0101");
        sampleAgent = new Agent(1L, "Sarah Connor", "sarah@support.com", "Technical Support");
    }

    @Test
    @DisplayName("Create Ticket - Success")
    void testCreateTicket_Success() {
        CreateTicketRequest request = new CreateTicketRequest("Bug in login", "Cannot login", TicketPriority.HIGH, 1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> {
            Ticket t = i.getArgument(0);
            t.setId(10L);
            return t;
        });

        Ticket created = ticketService.createTicket(request);

        assertNotNull(created);
        assertEquals(TicketStatus.OPEN, created.getStatus());
        assertEquals("Bug in login", created.getTitle());
        assertEquals(sampleCustomer, created.getCustomer());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Create Ticket - Customer Not Found (Throws NoSuchElementException)")
    void testCreateTicket_CustomerNotFound() {
        CreateTicketRequest request = new CreateTicketRequest("Bug", "Desc", TicketPriority.LOW, 999L);
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> ticketService.createTicket(request));
    }

    @Test
    @DisplayName("Assign Ticket - Success (OPEN to ASSIGNED)")
    void testAssignTicket_Success() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.MEDIUM, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(agentRepository.findById(1L)).thenReturn(Optional.of(sampleAgent));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

        Ticket assigned = ticketService.assignTicket(1L, 1L);

        assertEquals(TicketStatus.ASSIGNED, assigned.getStatus());
        assertEquals(sampleAgent, assigned.getAgent());
    }

    @Test
    @DisplayName("Assign Ticket - Agent Not Found (Throws NoSuchElementException)")
    void testAssignTicket_AgentNotFound() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.MEDIUM, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(agentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> ticketService.assignTicket(1L, 999L));
    }

    @Test
    @DisplayName("Assign Ticket - Closed Ticket Cannot Be Assigned (Throws IllegalStateException)")
    void testAssignTicket_ClosedTicket_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.MEDIUM, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> ticketService.assignTicket(1L, 1L));
    }

    @Test
    @DisplayName("Full Valid Workflow Transition: OPEN -> ASSIGNED -> IN_PROGRESS -> RESOLVED -> CLOSED")
    void testValidTransitions_FullWorkflow() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(agentRepository.findById(1L)).thenReturn(Optional.of(sampleAgent));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

        // Step 1: Assign (OPEN -> ASSIGNED)
        Ticket step1 = ticketService.assignTicket(1L, 1L);
        assertEquals(TicketStatus.ASSIGNED, step1.getStatus());

        // Step 2: In Progress (ASSIGNED -> IN_PROGRESS)
        Ticket step2 = ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, step2.getStatus());

        // Step 3: Resolve (IN_PROGRESS -> RESOLVED)
        Ticket step3 = ticketService.resolveTicket(1L, "Fixed in production hotfix v1.0.1");
        assertEquals(TicketStatus.RESOLVED, step3.getStatus());
        assertEquals("Fixed in production hotfix v1.0.1", step3.getResolutionNotes());

        // Step 4: Close (RESOLVED -> CLOSED)
        Ticket step4 = ticketService.closeTicket(1L);
        assertEquals(TicketStatus.CLOSED, step4.getStatus());
    }

    @Test
    @DisplayName("Invalid Transition: Direct OPEN to RESOLVED (Throws IllegalStateException)")
    void testInvalidTransition_OpenToResolved_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> ticketService.updateStatus(1L, TicketStatus.RESOLVED));
    }

    @Test
    @DisplayName("Invalid Transition: Cannot Reopen CLOSED Ticket (Throws IllegalStateException)")
    void testInvalidTransition_ClosedTicketModification_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> ticketService.updateStatus(1L, TicketStatus.OPEN));
    }

    @Test
    @DisplayName("Resolve Ticket - Missing Resolution Notes (Throws IllegalArgumentException)")
    void testResolveTicket_MissingResolutionNotes_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalArgumentException.class, () -> ticketService.resolveTicket(1L, "   "));
    }

    @Test
    @DisplayName("Resolve Ticket - Not IN_PROGRESS (Throws IllegalStateException)")
    void testResolveTicket_NotInProgress_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> ticketService.resolveTicket(1L, "Resolved notes"));
    }

    @Test
    @DisplayName("Close Ticket - Not RESOLVED (Throws IllegalStateException)")
    void testCloseTicket_NotResolved_ThrowsException() {
        Ticket ticket = new Ticket("Issue", "Desc", TicketPriority.HIGH, sampleCustomer);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(IllegalStateException.class, () -> ticketService.closeTicket(1L));
    }

    @Test
    @DisplayName("Get Stats - Correct Aggregation")
    void testGetStats_CalculatesCorrectCounts() {
        when(ticketRepository.count()).thenReturn(10L);
        when(ticketRepository.countByStatus(TicketStatus.OPEN)).thenReturn(3L);
        when(ticketRepository.countByStatus(TicketStatus.ASSIGNED)).thenReturn(1L);
        when(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS)).thenReturn(2L);
        when(ticketRepository.countByStatus(TicketStatus.RESOLVED)).thenReturn(2L);
        when(ticketRepository.countByStatus(TicketStatus.CLOSED)).thenReturn(2L);

        TicketStatsResponse stats = ticketService.getStats();

        assertEquals(10L, stats.getTotal());
        assertEquals(3L, stats.getOpen());
        assertEquals(1L, stats.getAssigned());
        assertEquals(2L, stats.getInProgress());
        assertEquals(2L, stats.getResolved());
        assertEquals(2L, stats.getClosed());
    }
}
