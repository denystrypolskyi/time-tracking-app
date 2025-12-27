package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.updatePasswordRequest;
import com.example.demo.dto.updateUsernameRequest;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserEntity createUser(LoginRequest user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        UserEntity existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(user.getPassword()));

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

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserEntity updateUsername(Long userId, updateUsernameRequest dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(dto.getUsername());
        return userRepository.save(user);
    }

    public void updatePassword(Long userId, updatePasswordRequest dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
