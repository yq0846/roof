package com.side.jiboong.domain.notice.response;

import com.side.jiboong.domain.notice.entity.NoticeComment;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NoticeCommentInfo(
        Long id,
        String comment,
        String username,
        ZonedDateTime createAt
) {
    public static NoticeCommentInfo from(NoticeComment comment) {
        return NoticeCommentInfo.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .username(comment.getUser().getUsername())
                .createAt(comment.getCreatedAt())
                .build();
    }
}
