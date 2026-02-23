package com.example.demo.dto;


import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ShiftResponse(@NotBlank Long id, @NotBlank LocalDateTime shiftStart, @NotBlank LocalDateTime shiftEnd) {
}
