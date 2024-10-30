package com.side.jiboong.infrastructure;

import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.domain.notice.entity.Notice;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<Notice> findAllBy(NoticeCondition condition);
    Integer countBy(NoticeCondition condition);
}
