package com.side.jiboong.infrastructure.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.jiboong.common.constant.SearchType;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.domain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import static com.side.jiboong.domain.notice.entity.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notice> findAllBy(NoticeCondition condition) {
        return queryFactory
                .selectFrom(notice)
                .where(
                        isEqualCategory(condition.category()),
                        betweenCreatedAt(condition),
                        containKeyword(condition)
                )
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();
    }

    @Override
    public Integer countBy(NoticeCondition condition) {
        return queryFactory
                .selectFrom(notice)
                .where(
                        isEqualCategory(condition.category()),
                        betweenCreatedAt(condition),
                        containKeyword(condition)
                )
                .fetch()
                .size();
    }

    private BooleanExpression isEqualCategory(NoticeCategory category) {
        return category != null
                ? notice.category.eq(category)
                : null;
    }

    private BooleanExpression betweenCreatedAt(NoticeCondition condition) {
        return (condition.startDate() != null && condition.endDate() != null)
                ? notice.createdAt.between(condition.startDate(), condition.endDate())
                : null;
    }

    private BooleanExpression containKeyword(NoticeCondition condition) {
        if(!StringUtils.hasText(condition.keyword())) {
            return null;
        }

        String keyword = condition.keyword();
        SearchType searchType = condition.searchType() == null
                ? SearchType.ALL
                : condition.searchType();

        return switch (searchType) {
            case TITLE -> notice.title.containsIgnoreCase(keyword);
            case CONTENT -> notice.contents.containsIgnoreCase(keyword);
            default -> notice.title.containsIgnoreCase(keyword)
                    .or(notice.contents.containsIgnoreCase(keyword));
        };
    }
}
