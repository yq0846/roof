package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.notice.dto.NoticeCondition;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

public class SearchDto {
    public record NoticeOptions(
            @Parameter(example = "2024-01-01T00:00:00.000000Z", description = "시작일")
            ZonedDateTime startDate,
            @Parameter(example = "2024-12-31T00:00:00.000000Z", description = "종료일")
            ZonedDateTime endDate
    ) {
        public NoticeCondition toNoticeCondition(
                Pageable pageable
        ) {
            return NoticeCondition.builder()
                    .pageable(pageable)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }
    }
}
