package com.support.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.dto.AssignTicketRequest;
import com.support.dto.CreateTicketRequest;
import com.support.dto.ResolveTicketRequest;
import com.support.dto.StatusUpdateRequest;
import com.support.entity.Agent;
import com.support.entity.Customer;
import com.support.entity.TicketPriority;
import com.support.entity.TicketStatus;
import com.support.repository.AgentRepository;
import com.support.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Test
    @DisplayName("API: Create Ticket - Success (201 Created)")
    void testCreateTicket_ApiSuccess() throws Exception {
        Customer customer = customerRepository.findAll().get(0);

        CreateTicketRequest request = new CreateTicketRequest(
                "Unable to generate monthly report",
                "Exporting monthly report produces a blank PDF file.",
                TicketPriority.HIGH,
                customer.getId()
        );

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("Unable to generate monthly report")))
                .andExpect(jsonPath("$.status", is("OPEN")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.customer.id", is(customer.getId().intValue())));
    }

    @Test
    @DisplayName("API: Create Ticket - Validation Failure (400 Bad Request)")
    void testCreateTicket_ValidationFailure() throws Exception {
        CreateTicketRequest invalidRequest = new CreateTicketRequest("", "", null, null);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Failed")));
    }

    @Test
    @DisplayName("API: Full Ticket Lifecycle (Create -> Assign -> In Progress -> Resolve -> Close)")
    void testFullTicketLifecycle() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        Agent agent = agentRepository.findAll().get(0);

        // 1. Create Ticket
        CreateTicketRequest createReq = new CreateTicketRequest(
                "Lifecycle Test Ticket",
                "Lifecycle Test Description",
                TicketPriority.URGENT,
                customer.getId()
        );

        String createResponse = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number ticketIdNum = com.jayway.jsonpath.JsonPath.read(createResponse, "$.id");
        long ticketId = ticketIdNum.longValue();

        // 2. Assign Ticket
        AssignTicketRequest assignReq = new AssignTicketRequest(agent.getId());
        mockMvc.perform(put("/api/tickets/" + ticketId + "/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ASSIGNED")))
                .andExpect(jsonPath("$.agent.id", is(agent.getId().intValue())));

        // 3. Move to IN_PROGRESS
        StatusUpdateRequest statusReq = new StatusUpdateRequest(TicketStatus.IN_PROGRESS);
        mockMvc.perform(put("/api/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

        // 4. Resolve Ticket
        ResolveTicketRequest resolveReq = new ResolveTicketRequest("Root cause identified and bug resolved.");
        mockMvc.perform(put("/api/tickets/" + ticketId + "/resolve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resolveReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("RESOLVED")))
                .andExpect(jsonPath("$.resolutionNotes", is("Root cause identified and bug resolved.")));

        // 5. Close Ticket
        mockMvc.perform(put("/api/tickets/" + ticketId + "/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CLOSED")));

        // 6. Attempt invalid transition on CLOSED ticket (Should fail with 400)
        mockMvc.perform(put("/api/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusUpdateRequest(TicketStatus.OPEN))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("API: Get Dashboard Stats (200 OK)")
    void testGetStats_ApiSuccess() throws Exception {
        mockMvc.perform(get("/api/tickets/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.open", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.assigned", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.inProgress", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.resolved", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.closed", greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("API: Search and Filter Tickets (200 OK)")
    void testSearchTickets() throws Exception {
        mockMvc.perform(get("/api/tickets/search")
                        .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("API: Get Customers and Agents (200 OK)")
    void testGetCustomersAndAgents() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/api/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
