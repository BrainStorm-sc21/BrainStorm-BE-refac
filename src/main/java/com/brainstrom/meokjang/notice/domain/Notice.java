package com.brainstrom.meokjang.notice.domain;

import com.brainstrom.meokjang.user.domain.User;
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "link_type", nullable = false)
    private Byte linkType;

    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "title", length = 15, nullable = false)
    private String title;

    @Column(name = "body", length = 45, nullable = false)
    private String body;
}
