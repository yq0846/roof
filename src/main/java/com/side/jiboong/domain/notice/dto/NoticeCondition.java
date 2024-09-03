package com.side.jiboong.domain.notice.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

@Builder
public record NoticeCondition(
        Pageable pageable,
        ZonedDateTime startDate,
        ZonedDateTime endDate
) { }
