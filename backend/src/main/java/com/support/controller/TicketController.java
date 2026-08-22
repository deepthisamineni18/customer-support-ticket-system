package com.support.controller;

import com.support.dto.AssignTicketRequest;
import com.support.dto.CreateTicketRequest;
import com.support.dto.ResolveTicketRequest;
import com.support.dto.StatusUpdateRequest;
import com.support.dto.TicketStatsResponse;
import com.support.entity.Ticket;
import com.support.entity.TicketPriority;
import com.support.entity.TicketStatus;
import com.support.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        Ticket createdTicket = ticketService.createTicket(request);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Ticket> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request) {
        return ResponseEntity.ok(ticketService.assignTicket(id, request.getAgentId()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(ticketService.updateStatus(id, request.getStatus()));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<Ticket> resolveTicket(
            @PathVariable Long id,
            @Valid @RequestBody ResolveTicketRequest request) {
        return ResponseEntity.ok(ticketService.resolveTicket(id, request.getResolutionNotes()));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Ticket> closeTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.closeTicket(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) Long agentId) {
        return ResponseEntity.ok(ticketService.searchTickets(keyword, status, priority, agentId));
    }

    @GetMapping("/stats")
    public ResponseEntity<TicketStatsResponse> getStats() {
        return ResponseEntity.ok(ticketService.getStats());
    }
}
