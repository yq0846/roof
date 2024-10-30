package com.side.jiboong.presentation.dto;

import com.side.jiboong.common.constant.SearchType;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Pageable;

public class SearchDto {
    public record NoticeOptions(
            @Parameter(example = "2024-01-01", description = "시작일")
            String startDate,
            @Parameter(example = "2024-12-31", description = "종료일")
            String endDate,
            @Parameter(description = "분류")
            NoticeCategory category,
            @Parameter(description = "조회유형")
            SearchType searchType,
            @Parameter(description = "검색어")
            String keyword
    ) {
        public NoticeCondition toNoticeCondition(
                Pageable pageable
        ) {
            return NoticeCondition.builder()
                    .pageable(pageable)
                    .startDate(this.startDate)
                    .endDate(this.endDate)
                    .category(this.category)
                    .searchType(this.searchType)
                    .keyword(this.keyword)
                    .build();
        }
    }
}
