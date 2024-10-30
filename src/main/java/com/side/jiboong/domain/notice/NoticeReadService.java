package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.domain.notice.request.NoticeCondition;
import com.side.jiboong.domain.notice.response.NoticeInfo;
import com.side.jiboong.domain.notice.response.NoticeItems;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.infrastructure.NoticeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("Notice not found."));
        return NoticeInfo.from(notice);
    }
}
