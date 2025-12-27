package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ShiftResponse;
import com.example.demo.model.ShiftEntity;

@Component
public class ShiftMapper {

    public ShiftResponse toDto(ShiftEntity entity) {
        ShiftResponse dto = new ShiftResponse();
        dto.setId(entity.getId());
        dto.setShiftStart(entity.getShiftStart());
        dto.setShiftEnd(entity.getShiftEnd());
        return dto;
    }
}
