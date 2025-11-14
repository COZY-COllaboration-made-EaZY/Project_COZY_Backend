package com.ohgiraffers.COZYbe.domain.recruit.entity;

import com.ohgiraffers.COZYbe.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_recruit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_Id")
    private Long recruitId;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "recruit_text")
    private String recruitText;

    @Column(name = "writer", nullable = false, length = 50)
    private String writer;


}
