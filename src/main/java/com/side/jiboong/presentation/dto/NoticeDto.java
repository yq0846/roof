package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.notice.entity.NoticeCategory;
import com.side.jiboong.domain.notice.request.NoticeCreate;
import com.side.jiboong.domain.notice.response.NoticeInfo;
import com.side.jiboong.domain.notice.response.NoticeItems;
import com.side.jiboong.domain.notice.request.NoticeUpdate;
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
            ZonedDateTime createAt
    ) {
        public static Items from(NoticeItems noticeItems) {
            return Items.builder()
                    .id(noticeItems.id())
                    .title(noticeItems.title())
                    .category(noticeItems.category())
                    .viewCount(noticeItems.viewCount())
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
                    .createAt(info.createAt())
                    .lastUpdatedAt(info.lastUpdatedAt())
                    .build();
        }
    }

    public record Create(
            String title,
            String contents,
            NoticeCategory category
    ) {
        public NoticeCreate toNoticeCreate() {
            return NoticeCreate.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .category(this.category)
                    .build();
        }
    }

    public record Update(
            String title,
            String contents,
            NoticeCategory category
    ) {
        public NoticeUpdate toNoticeUpdate() {
            return NoticeUpdate.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .category(this.category)
                    .build();
        }
    }

    public record Delete(
            List<Long> idList
    ) {}
}
