package com.side.jiboong.domain.notice.request;

import com.side.jiboong.domain.notice.entity.NoticeCategory;
import lombok.Builder;

@Builder
public record NoticeUpdate(
        String title,
        String contents,
        NoticeCategory category
) { }
