package com.example.demo.dto;

import com.example.demo.model.Role;
import jakarta.validation.constraints.NotBlank;

public record UserResponse(@NotBlank Long id, @NotBlank String username, @NotBlank Role role) {
}
