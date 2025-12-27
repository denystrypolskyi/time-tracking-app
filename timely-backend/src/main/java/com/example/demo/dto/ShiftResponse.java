package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftResponse {
    private Long id;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
}
