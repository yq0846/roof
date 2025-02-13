package com.side.jiboong.infrastructure.notice;

import com.side.jiboong.domain.notice.entity.NoticeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
    void deleteByNoticeId(Long noticeId);
    List<NoticeFile> findByNoticeId(Long noticeId);
}
