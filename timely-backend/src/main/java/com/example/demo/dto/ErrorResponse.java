package com.example.demo.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message, String detailedMessage, LocalDateTime timestamp
        ) {
}
