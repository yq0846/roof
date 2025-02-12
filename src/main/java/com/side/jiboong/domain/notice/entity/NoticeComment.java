package com.side.jiboong.domain.notice.entity;

import com.side.jiboong.domain.BaseEntity;
import com.side.jiboong.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public NoticeComment(
            Long id,
            String comment,
            Notice notice,
            User user
    ) {
        this.id = id;
        this.comment = comment;
        this.notice = notice;
        this.user = user;
    }
}
