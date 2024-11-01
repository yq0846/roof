package com.side.jiboong.infrastructure.notice;

import com.side.jiboong.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    void deleteByIdIn(List<Long> list);
}
