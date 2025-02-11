package com.side.jiboong.presentation.usecase;

import com.side.jiboong.common.annotation.UseCase;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.domain.user.UserReadService;
import com.side.jiboong.presentation.dto.AlarmDto;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class AlarmUseCase {
    private final RedisCacheManager redisCacheManager;
    private final UserReadService userReadService;

    public void register(AlarmDto alarmDto, Duration duration) {
        if (duration == null) {
            throw new IllegalArgumentException("기간을 입력바랍니다.");
        }

        List<Long> userIdList = userReadService.getAllUserIdList();

        userIdList.forEach(userId -> registerAlarm(alarmDto, userId, duration));
    }

    private void registerAlarm(AlarmDto alarmDto, Long userId, Duration duration) {
        switch (alarmDto.getType()) {
            case POSTING:
                registerPostingAlarm(alarmDto, userId, duration);
                break;

            default:
                throw new IllegalArgumentException("알람타입이 유효하지 않습니다.");
        }
    }

    private void registerPostingAlarm(AlarmDto alarmDto, Long userId, Duration duration) {
        alarmDto.setUserId(userId);
        redisCacheManager.setObjectValue(alarmDto.getHashKey(), alarmDto, duration);
    }
}
