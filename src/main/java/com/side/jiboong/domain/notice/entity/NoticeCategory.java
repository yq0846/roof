package com.side.jiboong.domain.notice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeCategory {
    FAQ("FAQ"),
    NOTICE("공지사항"),
    ;

    private final String description;
}
