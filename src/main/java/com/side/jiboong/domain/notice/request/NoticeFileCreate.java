package com.side.jiboong.domain.notice.request;

import com.side.jiboong.common.constant.FileCategory;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeFile;

public record NoticeFileCreate(
        String originalFileName,
        String encodedFileName,
        FileCategory fileCategory
) {
    public NoticeFile toNoticeFile(Notice notice) {
        return NoticeFile.builder()
                .originalFileName(this.originalFileName)
                .encodedFileName(this.encodedFileName)
                .fileCategory(this.fileCategory)
                .notice(notice)
                .build();
    }
}
