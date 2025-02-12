package com.side.jiboong.infrastructure.notice;

import com.side.jiboong.domain.notice.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
}
