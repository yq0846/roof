package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.domain.notice.request.NoticeCreate;
import com.side.jiboong.domain.notice.request.NoticeUpdate;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.infrastructure.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@WriteService
@RequiredArgsConstructor
public class NoticeWriteService {
    private final NoticeRepository noticeRepository;

    public void create(NoticeCreate create) {
        noticeRepository.save(create.toNotice());
    }

    public void update(Long id, NoticeUpdate update) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found."));
        notice.update(update.title(), update.contents(), update.category());
    }

    public void delete(List<Long> idList) {
        noticeRepository.deleteByIdIn(idList);
    }

    public void increaseViewCount(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found."));
        notice.increaseViewCount();
    }
}
