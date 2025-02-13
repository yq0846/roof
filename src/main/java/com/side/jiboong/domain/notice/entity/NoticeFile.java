package com.side.jiboong.domain.notice.entity;

import com.side.jiboong.common.constant.FileCategory;
import com.side.jiboong.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String originalFileName;

    private String encodedFileName;

    private FileCategory fileCategory;

    @Builder
    public NoticeFile(
            Long id,
            Notice notice,
            String originalFileName,
            String encodedFileName,
            FileCategory fileCategory
    ) {
        this.id = id;
        this.notice = notice;
        this.originalFileName = originalFileName;
        this.encodedFileName = encodedFileName;
        this.fileCategory = fileCategory;
    }

    public void update(
            String originalFileName,
            String encodedFileName,
            FileCategory fileCategory
    ) {
        this.originalFileName = originalFileName;
        this.encodedFileName = encodedFileName;
        this.fileCategory = fileCategory;
    }
}
