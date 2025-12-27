package com.example.demo.service;

import com.example.demo.model.ShiftEntity;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    public ShiftService(ShiftRepository shiftRepository, UserRepository userRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
    }

    public List<ShiftEntity> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<List<ShiftEntity>> getShiftsByUser(Long userId) {
        return userRepository.findById(userId).map(shiftRepository::findByUser);
    }

    public Optional<ShiftEntity> createShift(Long userId, LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        return userRepository.findById(userId).map(user -> {
            ShiftEntity shift = new ShiftEntity();
            shift.setUser(user);
            shift.setShiftStart(shiftStart);
            shift.setShiftEnd(shiftEnd);
            return shiftRepository.save(shift);
        });
    }

    public boolean deleteShift(Long id) {
        if (shiftRepository.existsById(id)) {
            shiftRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<List<ShiftEntity>> getShiftsByUserAndMonth(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return shiftRepository.findByUserIdAndShiftStartBetween(userId, startDate, endDate);
    }
}
