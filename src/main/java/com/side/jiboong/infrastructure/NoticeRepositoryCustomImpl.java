package com.side.jiboong.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.side.jiboong.domain.notice.dto.NoticeCondition;
import com.side.jiboong.domain.notice.entity.Notice;
import lombok.RequiredArgsConstructor;

import java.util.List;
import static com.side.jiboong.domain.notice.entity.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notice> findAllBy(NoticeCondition condition) {
        return queryFactory
                .selectFrom(notice)
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();
    }
}
