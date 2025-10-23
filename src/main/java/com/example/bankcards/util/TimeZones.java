package com.example.bankcards.util;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.EnumNamingStrategy;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.*;

@EnumNaming(value = EnumNamingStrategy.class)
@Getter
@AllArgsConstructor
public enum TimeZones {
    KALININGRAD_MSK_MINUS_1("Калининград", -1, 2),
    MOSKOW("Москва", 0, 3),
    SAMARA_MSK_PLUS_1("Самара", 1, 4),
    EKATERINBURG_MSK_PLUS_2("Екатеринбург", 2, 5),
    OMSK_MSK_PLUS_3("Омск", 3, 6),
    KRASNOYARSK_MSK_PLUS_4("Красноярск", 4, 7),
    IRKUTSK_MSK_PLUS_5("Иркутск", 5, 8),
    YAKUTSK_MSK_PLUS_6("Якутск", 6, 9),
    VLADIVOSTOK_MSK_PLUS_7("Владивосток", 7, 10),
    MAGADAN_MSK_PLUS_8("Магадан", 8, 11),
    PETROPAVLOVSK_KAMCHATSKI_MSK_PLUS_9("Петропавловск-Камчатский", 9, 12);

    private final String cityName;
    private final int mskOffset;
    private final int utcOffset;

    public Instant atStartOfDay(LocalDate day) {
        LocalDateTime localDateTime = day.atStartOfDay();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.ofHours(this.utcOffset));
        return offsetDateTime.toInstant();
    }

    @Override
    public String toString() {
        String msk = this.mskOffset >= 0 ? ": MSK+" : ": MSK";
        return cityName + msk + mskOffset + ", UTC+" + utcOffset;
    }

    @JsonValue // без данного метода swagger выводит строку из toString() и контроллер не может идентифицировать по ней timeZone
    public String getDescription() {
        return this.name();
    }
}
