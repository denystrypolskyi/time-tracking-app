package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ApiResponse(@NotBlank String message) {
}
