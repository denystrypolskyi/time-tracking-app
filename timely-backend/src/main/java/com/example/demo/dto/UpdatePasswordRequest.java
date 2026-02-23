package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) {}
