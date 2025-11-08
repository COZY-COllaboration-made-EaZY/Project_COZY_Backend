package com.ohgiraffers.COZYbe.domain.projects.entity;

import com.ohgiraffers.COZYbe.common.BaseTimeEntity;
//import com.ohgiraffers.COZYbe.domain.task.entity.Task;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tbl_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="project_id", columnDefinition="BINARY(16)")
    private UUID projectId;

    @Column(name = "projectName", nullable = false, unique = true, length = 100)
    private String projectName;

    @Column(name = "devInterest", nullable = false, length = 50)
    private String devInterest;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "gitHubUrl", nullable = true, length = 800)
    private String gitHubUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamid", nullable = false)
    private Team team;
}
