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
    private String details;

    private Long viewCount = 0L;

    @Builder
    public Notice(
            Long id,
            String title,
            String details,
            Long viewCount
    ) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.viewCount = viewCount;
    }

    public void update(
            String title,
            String details
    ) {
        this.title = title;
        this.details = details;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
