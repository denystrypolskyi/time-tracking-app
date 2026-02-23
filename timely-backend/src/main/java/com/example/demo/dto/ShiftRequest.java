package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ShiftRequest(@NotBlank String shiftStart, @NotBlank String shiftEnd) {
}