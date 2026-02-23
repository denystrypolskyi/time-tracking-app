package com.example.demo.mapper;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.Role;
import com.example.demo.model.UserEntity;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole() != null ? user.getRole() : Role.USER
        );
    }
}
