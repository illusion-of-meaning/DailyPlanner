package com.asherbakov.enums;

import java.time.LocalDate;

public enum Repeat {
    once("однократно"),
    daily("ежедневно"),
    weekly("еженедельно"),
    monthly("ежемесячно"),
    yearly("ежегодно");

    private final String period;

    Repeat(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
