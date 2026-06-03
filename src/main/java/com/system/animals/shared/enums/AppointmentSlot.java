package com.system.animals.shared.enums;

import java.time.LocalTime;

public enum AppointmentSlot {
    EIGHT_00(LocalTime.of(8, 0)),
    EIGHT_30(LocalTime.of(8, 30)),
    NINE_00(LocalTime.of(9, 0)),
    NINE_30(LocalTime.of(9, 30)),
    TEN_00(LocalTime.of(10, 0)),
    TEN_30(LocalTime.of(10, 30)),
    ELEVEN_00(LocalTime.of(11, 0)),
    ELEVEN_30(LocalTime.of(11, 30)),
    TWELVE_00(LocalTime.of(12, 0)),
    TWELVE_30(LocalTime.of(12, 30)),
    THIRTEEN_00(LocalTime.of(13, 0)),
    THIRTEEN_30(LocalTime.of(13, 30)),
    FOURTEEN_00(LocalTime.of(14, 0)),
    FOURTEEN_30(LocalTime.of(14, 30)),
    FIFTEEN_00(LocalTime.of(15, 0)),
    FIFTEEN_30(LocalTime.of(15, 30)),
    SIXTEEN_00(LocalTime.of(16, 0)),
    SIXTEEN_30(LocalTime.of(16, 30));

    private final LocalTime time;

    AppointmentSlot(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }
}