package com.ohgiraffers.COZYbe.domain.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProjectDTO {
    private String projectName;
    private String devInterest;
    private String description;
    private String gitHubUrl;
    private String leaderName;
}

