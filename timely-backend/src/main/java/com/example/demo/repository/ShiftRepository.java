package com.example.demo.repository;

import com.example.demo.model.ShiftEntity;
import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {
    List<ShiftEntity> findByUser(UserEntity user);
    Optional<List<ShiftEntity>> findByUserIdAndShiftStartBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
