package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameRequest(@NotBlank String username) {
}