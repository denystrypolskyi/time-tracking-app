package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String email) {
}
