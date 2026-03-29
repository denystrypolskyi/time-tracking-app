package com.example.demo.dto;

import com.example.demo.model.Role;

import java.time.LocalDateTime;

public record UserResponse(Long id, String username, String email, String fullName, Role role, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {

}
