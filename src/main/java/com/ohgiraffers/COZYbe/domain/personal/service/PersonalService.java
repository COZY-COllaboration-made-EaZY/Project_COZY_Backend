package com.ohgiraffers.COZYbe.domain.personal.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.personal.dto.*;
import com.ohgiraffers.COZYbe.domain.personal.entity.PersonalMemo;
import com.ohgiraffers.COZYbe.domain.personal.entity.PersonalPost;
import com.ohgiraffers.COZYbe.domain.personal.entity.PersonalSchedule;
import com.ohgiraffers.COZYbe.domain.personal.entity.RecurrenceType;
import com.ohgiraffers.COZYbe.domain.personal.repository.PersonalMemoRepository;
import com.ohgiraffers.COZYbe.domain.personal.repository.PersonalPostRepository;
import com.ohgiraffers.COZYbe.domain.personal.repository.PersonalScheduleRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalService {

    private final UserDomainService userDomainService;
    private final PersonalMemoRepository memoRepository;
    private final PersonalScheduleRepository scheduleRepository;
    private final PersonalPostRepository postRepository;

    private UUID toUserId(String userId) {
        return UUID.fromString(userId);
    }

    private User userRef(String userId) {
        return userDomainService.getReference(userId);
    }

    private String joinTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;
        return tags.stream()
                .filter(t -> t != null && !t.isBlank())
                .map(String::trim)
                .distinct()
                .collect(Collectors.joining(","));
    }

    private List<String> splitTags(String tags) {
        if (tags == null || tags.isBlank()) return List.of();
        return List.of(tags.split(",")).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    // ===== Memo =====
    public List<PersonalMemoResponse> getMemos(String userId) {
        UUID uid = toUserId(userId);
        return memoRepository.findAllByUser_UserIdOrderByCreatedAtDesc(uid).stream()
                .map(m -> new PersonalMemoResponse(
                        m.getMemoId(),
                        m.getTitle(),
                        m.getContent(),
                        splitTags(m.getTags()),
                        m.getCreatedAt(),
                        m.getUpdatedDate()
                ))
                .toList();
    }

    @Transactional
    public PersonalMemoResponse createMemo(PersonalMemoCreateDTO dto, String userId) {
        PersonalMemo memo = PersonalMemo.builder()
                .user(userRef(userId))
                .title(dto.title() == null || dto.title().isBlank() ? "메모" : dto.title())
                .content(dto.content() == null ? "" : dto.content())
                .tags(joinTags(dto.tags()))
                .build();
        PersonalMemo saved = memoRepository.save(memo);
        return new PersonalMemoResponse(
                saved.getMemoId(),
                saved.getTitle(),
                saved.getContent(),
                splitTags(saved.getTags()),
                saved.getCreatedAt(),
                saved.getUpdatedDate()
        );
    }

    @Transactional
    public PersonalMemoResponse updateMemo(UUID memoId, PersonalMemoUpdateDTO dto, String userId) {
        UUID uid = toUserId(userId);
        PersonalMemo memo = memoRepository.findByMemoIdAndUser_UserId(memoId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_MEMO));
        if (dto.title() != null && !dto.title().isBlank()) {
            memo.setTitle(dto.title());
        }
        if (dto.content() != null) {
            memo.setContent(dto.content());
        }
        if (dto.tags() != null) {
            memo.setTags(joinTags(dto.tags()));
        }
        return new PersonalMemoResponse(
                memo.getMemoId(),
                memo.getTitle(),
                memo.getContent(),
                splitTags(memo.getTags()),
                memo.getCreatedAt(),
                memo.getUpdatedDate()
        );
    }

    @Transactional
    public void deleteMemo(UUID memoId, String userId) {
        UUID uid = toUserId(userId);
        PersonalMemo memo = memoRepository.findByMemoIdAndUser_UserId(memoId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_MEMO));
        memoRepository.delete(memo);
    }

    // ===== Schedule =====
    public List<PersonalScheduleResponse> getSchedules(String userId) {
        UUID uid = toUserId(userId);
        return scheduleRepository.findAllByUser_UserIdOrderByStartAtAsc(uid).stream()
                .map(s -> new PersonalScheduleResponse(
                        s.getScheduleId(),
                        s.getTitle(),
                        s.getDescription(),
                        s.getLocation(),
                        s.getStartAt(),
                        s.getEndAt(),
                        s.getAllDay(),
                        s.getRecurrenceType(),
                        s.getRecurrenceInterval(),
                        s.getRecurrenceUntil(),
                        s.getRecurrenceCount(),
                        s.getCreatedAt(),
                        s.getUpdatedDate()
                ))
                .toList();
    }

    @Transactional
    public PersonalScheduleResponse createSchedule(PersonalScheduleCreateDTO dto, String userId) {
        validateSchedule(dto.startAt(), dto.endAt());
        RecurrenceType recurrenceType = dto.recurrenceType() != null ? dto.recurrenceType() : RecurrenceType.NONE;
        Integer interval = dto.recurrenceInterval() != null ? dto.recurrenceInterval() : 1;
        validateRecurrence(recurrenceType, interval, dto.recurrenceUntil(), dto.recurrenceCount(), dto.startAt().toLocalDate());
        PersonalSchedule schedule = PersonalSchedule.builder()
                .user(userRef(userId))
                .title(dto.title() == null || dto.title().isBlank() ? "일정" : dto.title())
                .description(dto.description())
                .location(dto.location())
                .startAt(dto.startAt())
                .endAt(dto.endAt())
                .allDay(dto.allDay() != null ? dto.allDay() : Boolean.FALSE)
                .recurrenceType(recurrenceType)
                .recurrenceInterval(interval)
                .recurrenceUntil(dto.recurrenceUntil())
                .recurrenceCount(dto.recurrenceCount())
                .build();
        PersonalSchedule saved = scheduleRepository.save(schedule);
        return new PersonalScheduleResponse(
                saved.getScheduleId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getLocation(),
                saved.getStartAt(),
                saved.getEndAt(),
                saved.getAllDay(),
                saved.getRecurrenceType(),
                saved.getRecurrenceInterval(),
                saved.getRecurrenceUntil(),
                saved.getRecurrenceCount(),
                saved.getCreatedAt(),
                saved.getUpdatedDate()
        );
    }

    @Transactional
    public PersonalScheduleResponse updateSchedule(UUID scheduleId, PersonalScheduleUpdateDTO dto, String userId) {
        UUID uid = toUserId(userId);
        PersonalSchedule schedule = scheduleRepository.findByScheduleIdAndUser_UserId(scheduleId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_SCHEDULE));

        if (dto.title() != null && !dto.title().isBlank()) {
            schedule.setTitle(dto.title());
        }
        if (dto.description() != null) {
            schedule.setDescription(dto.description());
        }
        if (dto.location() != null) {
            schedule.setLocation(dto.location());
        }
        if (dto.startAt() != null) {
            schedule.setStartAt(dto.startAt());
        }
        if (dto.endAt() != null) {
            schedule.setEndAt(dto.endAt());
        }
        if (dto.allDay() != null) {
            schedule.setAllDay(dto.allDay());
        }
        if (dto.recurrenceType() != null) {
            schedule.setRecurrenceType(dto.recurrenceType());
        }
        if (dto.recurrenceInterval() != null) {
            schedule.setRecurrenceInterval(dto.recurrenceInterval());
        }
        if (dto.recurrenceUntil() != null || dto.recurrenceCount() != null) {
            schedule.setRecurrenceUntil(dto.recurrenceUntil());
            schedule.setRecurrenceCount(dto.recurrenceCount());
        }

        validateSchedule(schedule.getStartAt(), schedule.getEndAt());
        RecurrenceType recurrenceType = schedule.getRecurrenceType() != null ? schedule.getRecurrenceType() : RecurrenceType.NONE;
        Integer interval = schedule.getRecurrenceInterval() != null ? schedule.getRecurrenceInterval() : 1;
        validateRecurrence(recurrenceType, interval, schedule.getRecurrenceUntil(), schedule.getRecurrenceCount(), schedule.getStartAt().toLocalDate());

        return new PersonalScheduleResponse(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getLocation(),
                schedule.getStartAt(),
                schedule.getEndAt(),
                schedule.getAllDay(),
                schedule.getRecurrenceType(),
                schedule.getRecurrenceInterval(),
                schedule.getRecurrenceUntil(),
                schedule.getRecurrenceCount(),
                schedule.getCreatedAt(),
                schedule.getUpdatedDate()
        );
    }

    @Transactional
    public void deleteSchedule(UUID scheduleId, String userId) {
        UUID uid = toUserId(userId);
        PersonalSchedule schedule = scheduleRepository.findByScheduleIdAndUser_UserId(scheduleId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_SCHEDULE));
        scheduleRepository.delete(schedule);
    }

    private void validateSchedule(java.time.LocalDateTime startAt, java.time.LocalDateTime endAt) {
        if (startAt == null || endAt == null || endAt.isBefore(startAt)) {
            throw new ApplicationException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }

    private void validateRecurrence(RecurrenceType type, Integer interval, java.time.LocalDate until, Integer count, java.time.LocalDate startDate) {
        if (type == null) return;
        if (type != RecurrenceType.NONE && (interval == null || interval < 1)) {
            throw new ApplicationException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
        if (until != null && until.isBefore(startDate)) {
            throw new ApplicationException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
        if (count != null && count < 1) {
            throw new ApplicationException(ErrorCode.INVALID_SCHEDULE_TIME);
        }
    }

    // ===== Personal Post =====
    public List<PersonalPostResponse> getPosts(String userId) {
        UUID uid = toUserId(userId);
        return postRepository.findAllByUser_UserIdOrderByCreatedAtDesc(uid).stream()
                .map(p -> new PersonalPostResponse(
                        p.getPostId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getCreatedAt(),
                        p.getUpdatedDate()
                ))
                .toList();
    }

    @Transactional
    public PersonalPostResponse createPost(PersonalPostCreateDTO dto, String userId) {
        PersonalPost post = PersonalPost.builder()
                .user(userRef(userId))
                .title(dto.title() == null || dto.title().isBlank() ? "개인 게시글" : dto.title())
                .content(dto.content() == null ? "" : dto.content())
                .build();
        PersonalPost saved = postRepository.save(post);
        return new PersonalPostResponse(
                saved.getPostId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedDate()
        );
    }

    @Transactional
    public PersonalPostResponse updatePost(UUID postId, PersonalPostUpdateDTO dto, String userId) {
        UUID uid = toUserId(userId);
        PersonalPost post = postRepository.findByPostIdAndUser_UserId(postId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PERSONAL_POST));
        if (dto.title() != null && !dto.title().isBlank()) {
            post.setTitle(dto.title());
        }
        if (dto.content() != null) {
            post.setContent(dto.content());
        }
        return new PersonalPostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedDate()
        );
    }

    @Transactional
    public void deletePost(UUID postId, String userId) {
        UUID uid = toUserId(userId);
        PersonalPost post = postRepository.findByPostIdAndUser_UserId(postId, uid)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PERSONAL_POST));
        postRepository.delete(post);
    }
}
