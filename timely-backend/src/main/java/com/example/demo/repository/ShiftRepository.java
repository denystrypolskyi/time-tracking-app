package com.example.demo.repository;

import com.example.demo.model.ShiftEntity;
import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {

    // Fetch shifts and join the user in one query
    @Query("""
                SELECT s FROM ShiftEntity s
                JOIN FETCH s.user
                WHERE s.user.id = :userId
            """)
    List<ShiftEntity> findByUserIdWithUser(@Param("userId") Long userId);

    // Optional: fetch by date range
    @Query("""
                SELECT s FROM ShiftEntity s
                JOIN FETCH s.user
                WHERE s.user.id = :userId
                  AND s.shiftStart BETWEEN :startDate AND :endDate
            """)
    List<ShiftEntity> findByUserIdAndShiftStartBetweenWithUser(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}