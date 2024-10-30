package com.side.jiboong.domain.notice.entity;

import com.side.jiboong.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8000)
    private String contents;

    @Enumerated(EnumType.STRING)
    private NoticeCategory category;

    private Long viewCount;

    @Builder
    public Notice(
            Long id,
            String title,
            String contents,
            NoticeCategory category,
            Long viewCount
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.viewCount = viewCount;
    }

    public void update(
            String title,
            String details,
            NoticeCategory category
    ) {
        this.title = title;
        this.contents = details;
        this.category = category;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
