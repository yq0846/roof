package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.domain.notice.dto.NoticeInfo;
import com.side.jiboong.domain.notice.dto.NoticeItems;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.infrastructure.NoticeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@ReadService
@RequiredArgsConstructor
public class NoticeReadService {
    private final NoticeRepository noticeRepository;

    public List<NoticeItems> findByAll() {
        return noticeRepository.findAll().stream()
                .map(NoticeItems::from)
                .toList();
    }

    public NoticeInfo findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found."));
        return NoticeInfo.from(notice);
    }
}
