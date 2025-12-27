package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ShiftRequest;
import com.example.demo.dto.ShiftResponse;
import com.example.demo.mapper.ShiftMapper;
import com.example.demo.service.AuthService;
import com.example.demo.service.ShiftService;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    private final ShiftService shiftService;
    private final AuthService authService;
    private final ShiftMapper shiftMapper;

    public ShiftController(ShiftService shiftService, AuthService authService, ShiftMapper shiftMapper) {
        this.shiftService = shiftService;
        this.authService = authService;
        this.shiftMapper = shiftMapper;
    }

    @GetMapping("/user")
    public ResponseEntity<List<ShiftResponse>> getShiftsForCurrentUser(
            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = extractUserIdFromHeader(authorizationHeader);

        return ResponseEntity.ok(
                shiftService.getShiftsByUser(userId)
                        .orElse(List.of())
                        .stream()
                        .map(shiftMapper::toDto)
                        .toList());

    }

    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ShiftRequest request) {

        Long userId = extractUserIdFromHeader(authorizationHeader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime shiftStart = LocalDateTime.parse(request.getShiftStart(), formatter);
        LocalDateTime shiftEnd = LocalDateTime.parse(request.getShiftEnd(), formatter);

        return shiftService.createShift(userId, shiftStart, shiftEnd)
                .map(shiftMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        return shiftService.deleteShift(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private Long extractUserIdFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String token = authorizationHeader.split(" ")[1];
        return authService.getUserIdFromToken(token);
    }

    @GetMapping("/user/{year}/{month}")
    public ResponseEntity<List<ShiftResponse>> getUserShiftsForMonth(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int year,
            @PathVariable int month) {

        Long userId = extractUserIdFromHeader(authorizationHeader);

        return shiftService.getShiftsByUserAndMonth(userId, year, month)
                .map(shifts -> shifts.stream()
                        .map(shiftMapper::toDto)
                        .toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
