package com.side.jiboong.infrastructure;

import com.side.jiboong.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    void deleteByIdIn(List<Long> list);
}
