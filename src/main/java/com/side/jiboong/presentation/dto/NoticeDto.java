package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.notice.entity.NoticeCategory;
import com.side.jiboong.domain.notice.request.*;
import com.side.jiboong.domain.notice.response.NoticeCommentInfo;
import com.side.jiboong.domain.notice.response.NoticeInfo;
import com.side.jiboong.domain.notice.response.NoticeItems;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

public class NoticeDto {
    @Builder
    public record Items(
            Long id,
            String title,
            NoticeCategory category,
            Long viewCount,
            Long commentCount,
            ZonedDateTime createAt
    ) {
        public static Items from(NoticeItems noticeItems) {
            return Items.builder()
                    .id(noticeItems.id())
                    .title(noticeItems.title())
                    .category(noticeItems.category())
                    .viewCount(noticeItems.viewCount())
                    .commentCount(noticeItems.commentCount())
                    .createAt(noticeItems.createAt())
                    .build();
        }
    }

    @Builder
    public record Info(
            Long id,
            String title,
            String contents,
            NoticeCategory category,
            Long viewCount,
            List<NoticeCommentInfo> comments,
            ZonedDateTime createAt,
            ZonedDateTime lastUpdatedAt
    ) {
        public static Info from(NoticeInfo info) {
            return Info.builder()
                    .id(info.id())
                    .title(info.title())
                    .contents(info.contents())
                    .category(info.category())
                    .viewCount(info.viewCount())
                    .comments(info.comments())
                    .createAt(info.createAt())
                    .lastUpdatedAt(info.lastUpdatedAt())
                    .build();
        }
    }

    public record Create(
            String title,
            String contents,
            NoticeCategory category,
            List<NoticeFileCreate> noticeFiles
    ) {
        public NoticeCreate toNoticeCreate() {
            return NoticeCreate.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .category(this.category)
                    .build();
        }

        public List<NoticeFileCreate> toFileCreate() {
            return this.noticeFiles;
        }
    }

    public record Update(
            String title,
            String contents,
            NoticeCategory category,
            List<NoticeFileUpdate> noticeFiles
    ) {
        public NoticeUpdate toNoticeUpdate() {
            return NoticeUpdate.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .category(this.category)
                    .build();
        }

        public List<NoticeFileUpdate> toFileUpdate() {
            return this.noticeFiles;
        }
    }

    public record Delete(
            List<Long> idList
    ) {}

    public record CommentCreate(
            String comment,
            Long noticeId
    ) {
        public NoticeCommentCreate toNoticeCommentCreate(Long userId) {
            return NoticeCommentCreate.builder()
                    .comment(this.comment)
                    .noticeId(this.noticeId)
                    .userId(userId)
                    .build();
        }
    }

    public record CommentDelete(
            Long id
    ) {}
}
