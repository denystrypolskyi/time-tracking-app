package com.example.demo.service;

import com.example.demo.model.ShiftEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
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

    /**
     * Retrieves all shifts for a given user.
     *
     * @param userId the ID of the user whose shifts should be retrieved
     * @return a {@link List} of {@link ShiftEntity}, empty if the user has no shifts
     * @throws IllegalArgumentException if userId is null or the user does not exist
     */
    public List<ShiftEntity> getShiftsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return shiftRepository.findByUser(user);
    }


    /**
     * Creates and persists a work shift for a user.
     *
     * @param userId     the ID of the user who owns the shift
     * @param shiftStart start date and time of the shift
     * @param shiftEnd   end date and time of the shift
     * @return the persisted {@link ShiftEntity}
     * @throws IllegalArgumentException if the user does not exist, if shift start or end is null, or if the end time is before the start time
     */
    public ShiftEntity createShift(Long userId,
                                   LocalDateTime shiftStart,
                                   LocalDateTime shiftEnd) {

        if (shiftStart == null) {
            throw new IllegalArgumentException("Shift start must be provided");
        }
        if (shiftEnd == null) {
            throw new IllegalArgumentException("Shift end must be provided");
        }
        if (shiftEnd.isBefore(shiftStart)) {
            throw new IllegalArgumentException("Shift end must be after shift start");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ShiftEntity shift = new ShiftEntity();
        shift.setUser(user);
        shift.setShiftStart(shiftStart);
        shift.setShiftEnd(shiftEnd);

        return shiftRepository.save(shift);
    }

    /**
     * Deletes a shift by its ID.
     *
     * @param id the ID of the shift to delete
     * @throws IllegalArgumentException if no shift with the given ID exists
     */
    public void deleteShift(Long id) {
        ShiftEntity shift = shiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

        shiftRepository.delete(shift);
    }


    /**
     * Retrieves all shifts for a given user within a specific month.
     *
     * @param userId the ID of the user whose shifts should be retrieved
     * @param year   the year of the desired month
     * @param month  the month (1-12) of the desired shifts
     * @return a {@link List} of {@link ShiftEntity} objects for the user in the specified month, or an empty list if no shifts exist
     * @throws IllegalArgumentException if userId is null, month is not between 1 and 12, or year is outside the valid range (1900-2100)
     */
    public List<ShiftEntity> getShiftsByUserAndMonth(Long userId, int year, int month) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (year < 1900 || year > 2100) { // optional range check
            throw new IllegalArgumentException("Year is out of valid range");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return shiftRepository.findByUserIdAndShiftStartBetween(userId, startDate, endDate)
                .orElse(Collections.emptyList());
    }
}
