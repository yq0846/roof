package com.side.jiboong.domain.notice.response;

import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NoticeInfo(
        Long id,
        String title,
        String contents,
        NoticeCategory category,
        Long viewCount,
        ZonedDateTime createAt,
        ZonedDateTime lastUpdatedAt
) {
    public static NoticeInfo from(Notice notice) {
        return NoticeInfo.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .category(notice.getCategory())
                .viewCount(notice.getViewCount())
                .createAt(notice.getCreatedAt())
                .lastUpdatedAt(notice.getLastUpdatedAt())
                .build();
    }
}
