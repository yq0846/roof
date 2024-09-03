package com.side.jiboong.domain.notice.dto;

import com.side.jiboong.domain.notice.entity.Notice;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NoticeItems(
        Long id,
        String title,
        Long viewCount,
        ZonedDateTime createAt
) {
    public static NoticeItems from(Notice notice) {
        return NoticeItems.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .viewCount(notice.getViewCount())
                .createAt(notice.getCreatedAt())
                .build();
    }
}
