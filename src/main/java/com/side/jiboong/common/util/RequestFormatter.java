package com.side.jiboong.common.util;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class RequestFormatter {
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static ZonedDateTime parseZonedDateTime(@NotNull String dateStr) {
        return ZonedDateTime.parse(dateStr + "T00:00:00.00000Z");
    }

    public static ZonedDateTime getMinZonedDateTime(@Nullable String dateStr) {
        return dateStr == null
                ? ZonedDateTime.of(LocalDate.EPOCH, LocalTime.MIN, ZONE_ID)
                : parseZonedDateTime(dateStr).with(LocalTime.MIN);
    }

    public static ZonedDateTime getMaxZonedDateTime(@Nullable String dateStr) {
        return dateStr == null
                ? ZonedDateTime.now().plusMonths(1).with(LocalTime.MAX)
                : parseZonedDateTime(dateStr).with(LocalTime.MAX);
    }
}
