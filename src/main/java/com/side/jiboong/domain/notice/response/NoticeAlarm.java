package com.side.jiboong.domain.notice.response;

import com.side.jiboong.common.constant.AlarmType;
import com.side.jiboong.common.constant.RedisKey;
import com.side.jiboong.domain.notice.entity.Notice;
import com.side.jiboong.domain.notice.entity.NoticeCategory;
import com.side.jiboong.presentation.dto.AlarmDto;
import lombok.Builder;

import java.time.ZonedDateTime;

public class NoticeAlarm implements AlarmDto {
    private final AlarmType type;
    private final NoticeCategory noticeCategory;
    private final Long noticeId;
    private final String noticeTitle;
    private final ZonedDateTime createdAt;
    private ZonedDateTime lastUpdatedAt;
    private Long userId;

    @Builder
    public NoticeAlarm(
            AlarmType type,
            NoticeCategory noticeCategory,
            Long noticeId,
            String noticeTitle,
            ZonedDateTime createdAt,
            ZonedDateTime lastUpdatedAt,
            Long userId
    ) {
        this.type = type;
        this.noticeCategory = noticeCategory;
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.userId = userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getHashKey() {
        return "%s|%s-%d|USER-%d".formatted(
                RedisKey.POSTING_ALARM,
                this.noticeCategory,
                this.noticeId,
                this.userId
        );
    }

    @Override
    public AlarmType getType() {
        return this.type;
    }

    @Override
    public String getTitle() {
        return this.noticeTitle;
    }

    @Override
    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public ZonedDateTime getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    public static NoticeAlarm from(Notice notice) {
        return NoticeAlarm.builder()
                .type(AlarmType.POSTING)
                .noticeCategory(notice.getCategory())
                .noticeId(notice.getId())
                .noticeTitle(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .lastUpdatedAt(notice.getLastUpdatedAt())
                .build();
    }
}
