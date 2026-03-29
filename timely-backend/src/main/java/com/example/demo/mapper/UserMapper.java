package com.example.demo.mapper;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toDTO(UserEntity user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
