package com.ohgiraffers.COZYbe.domain.teams.domain.entity;

import com.ohgiraffers.COZYbe.common.BaseTimeEntity;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Team extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID teamId;

    @Setter
    @Column(nullable = false, unique = true)
    private String teamName;

    @Setter
    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User leader;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User subLeader;

    @Column
    private Boolean isDisabled;

    public void disableTeam(){
        this.isDisabled = true;
    }

}
