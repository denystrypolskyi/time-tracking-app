package com.example.demo.controller;

import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UpdatePasswordRequest;
import com.example.demo.dto.UpdateUsernameRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.mapper.UserMapper;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserEntity;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    public final UserService userService;
    public final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse createUser(@RequestBody @Valid LoginRequest user) {
        userService.createUser(user);
        return new ApiResponse("User registered successfully");
    }

    @DeleteMapping("/{userId}")
    public ApiResponse deleteUserById(@PathVariable Long userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return new ApiResponse("User deleted successfully");
    }


    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginDTO) {
        try {
            String token = authService.login(loginDTO);
            return new TokenResponse(token);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @GetMapping()
    public List<UserResponse> getUsers() {
        return userService.getUsers()
                .stream()
                .map(
                        UserMapper::toResponse
                ).toList();
    }

    @PatchMapping("/username")
    public ApiResponse updateUsername(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid UpdateUsernameRequest dto) {
        userService.updateUsername(user.getId(), dto);

        return new ApiResponse("Username updated successfully");
    }

    @PatchMapping("/password")
    public ApiResponse updatePassword(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid UpdatePasswordRequest dto) {

        userService.updatePassword(user.getId(), dto);

        return new ApiResponse("Password updated successfully");
    }

    @GetMapping("/profile")
    public UserResponse getLoggedInUser(@AuthenticationPrincipal CustomUserDetails user) {
        UserEntity foundUser = userService.getUserById(user.getId());
        return new UserResponse(foundUser.getId(), foundUser.getUsername(), foundUser.getRole());
    }
}