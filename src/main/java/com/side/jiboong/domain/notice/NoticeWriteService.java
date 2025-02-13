package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.common.exception.NotFoundException;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.request.NoticeCreate;
import com.side.jiboong.domain.notice.request.NoticeUpdate;
import com.side.jiboong.infrastructure.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@WriteService
@RequiredArgsConstructor
public class NoticeWriteService {
    private final NoticeRepository noticeRepository;

    public Notice create(NoticeCreate create) {
        return noticeRepository.save(create.toNotice());
    }

    public Notice update(Long id, NoticeUpdate update) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        notice.update(update.title(), update.contents(), update.category());
        return notice;
    }

    public void delete(List<Long> idList) {
        noticeRepository.deleteByIdIn(idList);
    }

    public void increaseViewCount(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        notice.increaseViewCount();
    }
}
