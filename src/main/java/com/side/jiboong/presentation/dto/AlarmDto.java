package com.side.jiboong.presentation.dto;

import com.side.jiboong.common.constant.AlarmType;

import java.time.ZonedDateTime;

public interface AlarmDto {
    void setUserId(Long userId);
    String getHashKey();
    AlarmType getType();
    String getTitle();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getLastUpdatedAt();
    Long getUserId();
}
