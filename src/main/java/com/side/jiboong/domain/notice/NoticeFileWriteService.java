package com.side.jiboong.domain.notice;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.common.exception.NotFoundException;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeFile;
import com.side.jiboong.domain.notice.request.NoticeFileCreate;
import com.side.jiboong.domain.notice.request.NoticeFileUpdate;
import com.side.jiboong.infrastructure.notice.NoticeFileRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@WriteService
@RequiredArgsConstructor
public class NoticeFileWriteService {
    private final NoticeFileRepository noticeFileRepository;

    public void create(Notice notice, List<NoticeFileCreate> creates) {
        List<NoticeFile> noticeFiles = creates.stream()
                .map(create -> create.toNoticeFile(notice))
                .toList();

        noticeFileRepository.saveAll(noticeFiles);
    }

    public void update(Notice existingNotice, List<NoticeFileUpdate> updates) {

        // 저장 및 수정
        updates.forEach(update -> {
            if (update.id() != null) {
                NoticeFile noticeFile = noticeFileRepository.findById(update.id())
                        .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));
                noticeFile.update(update.originalFileName(), update.encodedFileName(), update.fileCategory());
            } else {
                noticeFileRepository.save(update.toNoticeFile(existingNotice));
            }
        });

        // 삭제
        List<NoticeFile> existingNoticeFile = noticeFileRepository.findByNoticeId(existingNotice.getId());
        existingNoticeFile.stream()
                .map(NoticeFile::getId)
                .filter(id -> updates.stream()
                        .noneMatch(updateFile -> updateFile.id() != null && updateFile.id().equals(id)))
                .forEach(noticeFileRepository::deleteById);
    }

    public void deleteAll(List<Long> noticeIdList) {
        noticeIdList.forEach(noticeFileRepository::deleteByNoticeId);
    }
}
