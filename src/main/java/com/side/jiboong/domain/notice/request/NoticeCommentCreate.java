package com.side.jiboong.domain.notice.request;

import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeComment;
import com.side.jiboong.domain.user.entity.User;
import lombok.Builder;

@Builder
public record NoticeCommentCreate(
        String comment,
        Long noticeId,
        Long userId
) {
    public NoticeComment toNoticeComment(Notice notice, User user) {
        return NoticeComment.builder()
                .comment(this.comment)
                .notice(notice)
                .user(user)
                .build();
    }
}
