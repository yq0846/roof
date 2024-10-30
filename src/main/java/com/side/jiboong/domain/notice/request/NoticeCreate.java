package com.side.jiboong.domain.notice.request;

import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import lombok.Builder;

@Builder
public record NoticeCreate(
        String title,
        String contents,
        NoticeCategory category
) {
    public Notice toNotice() {
        return Notice.builder()
                .title(this.title)
                .contents(this.contents)
                .category(this.category)
                .viewCount(0L)
                .build();
    }
}
