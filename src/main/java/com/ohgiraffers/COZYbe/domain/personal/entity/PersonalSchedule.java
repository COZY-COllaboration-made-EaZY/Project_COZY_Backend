package com.ohgiraffers.COZYbe.domain.personal.entity;

import com.ohgiraffers.COZYbe.common.BaseTimeEntity;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "tbl_personal_schedule")
public class PersonalSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id")
    private UUID scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @Column(nullable = false, length = 200)
    private String title;

    @Setter
    @Column(length = 2000)
    private String description;

    @Setter
    @Column(length = 200)
    private String location;

    @Setter
    @Column(nullable = false)
    private LocalDateTime startAt;

    @Setter
    @Column(nullable = false)
    private LocalDateTime endAt;

    @Setter
    @Column(nullable = false)
    private Boolean allDay;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecurrenceType recurrenceType;

    @Setter
    @Column(nullable = false)
    private Integer recurrenceInterval;

    @Setter
    @Column
    private LocalDate recurrenceUntil;

    @Setter
    @Column
    private Integer recurrenceCount;
}
