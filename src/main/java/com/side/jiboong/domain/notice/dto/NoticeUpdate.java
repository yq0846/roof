package com.side.jiboong.domain.notice.dto;

import lombok.Builder;

@Builder
public record NoticeUpdate(
        String title,
        String details
) { }
