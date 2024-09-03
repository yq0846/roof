package com.side.jiboong.domain.notice.dto;

import com.side.jiboong.domain.notice.entity.Notice;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NoticeInfo(
        Long id,
        String title,
        String details,
        Long viewCount,
        ZonedDateTime createAt,
        ZonedDateTime lastUpdatedAt
) {
    public static NoticeInfo from(Notice notice) {
        return NoticeInfo.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .details(notice.getDetails())
                .viewCount(notice.getViewCount())
                .createAt(notice.getCreatedAt())
                .lastUpdatedAt(notice.getLastUpdatedAt())
                .build();
    }
}
