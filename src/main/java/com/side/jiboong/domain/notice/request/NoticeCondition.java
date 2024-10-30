package com.side.jiboong.domain.notice.request;

import com.side.jiboong.common.constant.SearchType;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

import static com.side.jiboong.common.util.RequestFormatter.getMaxZonedDateTime;
import static com.side.jiboong.common.util.RequestFormatter.getMinZonedDateTime;

public record NoticeCondition(
        Pageable pageable,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        NoticeCategory category,
        SearchType searchType,
        String keyword
) {
    @Builder
    public NoticeCondition(
            Pageable pageable,
            String startDate,
            String endDate,
            NoticeCategory category,
            SearchType searchType,
            String keyword
    ) {
        this(
                pageable,
                getMinZonedDateTime(startDate),
                getMaxZonedDateTime(endDate),
                category,
                searchType,
                keyword
        );
    }
}
