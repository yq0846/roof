package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.common.exception.NotFoundException;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.domain.notice.response.NoticeInfo;
import com.side.jiboong.domain.notice.response.NoticeItems;
import com.side.jiboong.infrastructure.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ReadService
@RequiredArgsConstructor
public class NoticeReadService {
    private final NoticeRepository noticeRepository;

    public List<NoticeItems> findByAll(NoticeCondition condition) {
        return noticeRepository.findAllBy(condition).stream()
                .map(NoticeItems::from)
                .toList();
    }

    public int countBy(NoticeCondition condition) {
        return noticeRepository.countBy(condition);
    }

    public NoticeInfo findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        return NoticeInfo.from(notice);
    }
}
