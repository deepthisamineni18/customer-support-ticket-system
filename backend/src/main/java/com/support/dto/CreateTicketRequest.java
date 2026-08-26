package com.support.dto;

import com.support.entity.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateTicketRequest {

    @NotBlank(message = "Title is required")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "Title must contain at least one letter")
    private String title;

    @NotBlank(message = "Description is required")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "Description must contain at least one letter")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    public CreateTicketRequest() {
    }

    public CreateTicketRequest(String title, String description, TicketPriority priority, Long customerId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
