package com.side.jiboong.domain.notice.response;

import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record NoticeItems(
        Long id,
        String title,
        NoticeCategory category,
        Long viewCount,
        Long commentCount,
        ZonedDateTime createAt
) {
    public static NoticeItems from(Notice notice) {
        return NoticeItems.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .viewCount(notice.getViewCount())
                .commentCount(notice.getComments() != null ? notice.getComments().size() : 0L)
                .createAt(notice.getCreatedAt())
                .build();
    }
}
