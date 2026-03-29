package com.example.demo.dto;

import java.time.LocalDateTime;

public record ShiftResponse(Long id, LocalDateTime shiftStart, LocalDateTime shiftEnd) {
}
