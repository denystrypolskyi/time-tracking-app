package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UpdatePasswordRequest;
import com.example.demo.dto.UpdateUsernameRequest;
import com.example.demo.model.Role;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public UserEntity createUser(LoginRequest user) {

        if (user.username() == null || user.username().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }

        if (user.password() == null || user.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }

        if (userRepository.existsByUsername(user.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.username());
        newUser.setPassword(encoder.encode(user.password()));
        newUser.setRole(Role.USER); // default role

        return userRepository.save(newUser);
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean deleteUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    public UserEntity updateUsername(Long userId, UpdateUsernameRequest dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(dto.username());
        return userRepository.save(user);
    }

    public void updatePassword(Long userId, UpdatePasswordRequest dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!encoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(encoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

}
