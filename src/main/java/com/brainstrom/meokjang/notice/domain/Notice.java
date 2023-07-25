package com.brainstrom.meokjang.notice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "NOTICE")
@Getter
@NoArgsConstructor
public class Notice {

    @Id @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "link_type", nullable = false)
    private Byte linkType;

    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "title", length = 45)
    private String title;

    @Column(name = "body", length = 45)
    private String body;
}
