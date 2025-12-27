package com.example.demo.controller;

import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.updatePasswordRequest;
import com.example.demo.dto.updateUsernameRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.model.UserEntity;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, String> createUser(@RequestBody LoginRequest user) {
        userService.createUser(user);
        return Map.of("message", "User registered successfully");
    }

    @DeleteMapping("/{userId}")
    public Map<String, String> deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return Map.of("message", "User deleted successfully");
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginDTO) {
        String token = authService.verify(loginDTO);
        return new TokenResponse(token);
    }

    @GetMapping("/")
    public List<UserResponse> getUsers() {
        return userService.getUsers()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getPassword())).toList();
    }

    @PatchMapping("/username")
    public Map<String, String> updateUsername(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody updateUsernameRequest dto) {
        String token = authorizationHeader.split(" ")[1];
        Long userId = authService.getUserIdFromToken(token);

        userService.updateUsername(userId, dto);
        return Map.of("message", "Username updated successfully");
    }

    @PatchMapping("/password")
    public Map<String, String> updatePassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody updatePasswordRequest dto) {
        String token = authorizationHeader.split(" ")[1];
        Long userId = authService.getUserIdFromToken(token);

        userService.updatePassword(userId, dto);
        return Map.of("message", "Password updated successfully");
    }

    @GetMapping("/profile")
    public UserResponse getLoggedInUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.split(" ")[1];
        Long userId = authService.getUserIdFromToken(token);
        UserEntity user = userService.getUserById(userId);

        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}