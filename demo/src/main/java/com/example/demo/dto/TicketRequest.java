package com.example.demo.dto;

public record TicketRequest(
        String description,
        String longText,
        String priority,
        TicketType type
) {
    public enum TicketType {
        incident,
        requirement
    }
}
