package com.ohgiraffers.COZYbe.domain.personal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.ohgiraffers.COZYbe.domain.personal.entity.RecurrenceType;

public record PersonalScheduleResponse(
        UUID scheduleId,
        String title,
        String description,
        String location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Boolean allDay,
        RecurrenceType recurrenceType,
        Integer recurrenceInterval,
        LocalDate recurrenceUntil,
        Integer recurrenceCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
