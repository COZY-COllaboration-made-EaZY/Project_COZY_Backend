package com.ohgiraffers.COZYbe.domain.personal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.ohgiraffers.COZYbe.domain.personal.entity.RecurrenceType;

public record PersonalScheduleUpdateDTO(
        String title,
        String description,
        String location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Boolean allDay,
        RecurrenceType recurrenceType,
        Integer recurrenceInterval,
        LocalDate recurrenceUntil,
        Integer recurrenceCount
) {
}
