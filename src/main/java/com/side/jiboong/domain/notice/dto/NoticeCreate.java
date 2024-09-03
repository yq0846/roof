package com.side.jiboong.domain.notice.dto;

import com.side.jiboong.domain.notice.entity.Notice;
import lombok.Builder;

@Builder
public record NoticeCreate(
        String title,
        String details,
        Long viewCount
) {
    public Notice toNotice() {
        return Notice.builder()
                .title(this.title)
                .details(this.details)
                .viewCount(this.viewCount)
                .build();
    }
}
