package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.notice.dto.NoticeCreate;
import com.side.jiboong.domain.notice.dto.NoticeInfo;
import com.side.jiboong.domain.notice.dto.NoticeItems;
import com.side.jiboong.domain.notice.dto.NoticeUpdate;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

public class NoticeDto {
    @Builder
    public record Items(
            Long id,
            String title,
            Long viewCount,
            ZonedDateTime createAt
    ) {
        public static Items from(NoticeItems noticeItems) {
            return Items.builder()
                    .id(noticeItems.id())
                    .title(noticeItems.title())
                    .viewCount(noticeItems.viewCount())
                    .createAt(noticeItems.createAt())
                    .build();
        }
    }

    @Builder
    public record Info(
            Long id,
            String title,
            String details,
            Long viewCount,
            ZonedDateTime createAt,
            ZonedDateTime lastUpdatedAt
    ) {
        public static Info from(NoticeInfo info) {
            return Info.builder()
                    .id(info.id())
                    .title(info.title())
                    .details(info.details())
                    .viewCount(info.viewCount())
                    .createAt(info.createAt())
                    .lastUpdatedAt(info.lastUpdatedAt())
                    .build();
        }
    }

    public record Create(
            String title,
            String details
    ) {
        public NoticeCreate toNoticeCreate() {
            return NoticeCreate.builder()
                    .title(this.title)
                    .details(this.details)
                    .viewCount(0L)
                    .build();
        }
    }

    public record Update(
            String title,
            String details
    ) {
        public NoticeUpdate toNoticeUpdate() {
            return NoticeUpdate.builder()
                    .title(this.title)
                    .details(this.details)
                    .build();
        }
    }

    public record Delete(
            List<Long> idList
    ) {}
}
