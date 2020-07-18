package com.shanebeestudios.bot.util;

public enum TimeFrame {

    MINUTE(60 * 1000, "minute", "m", "mins", "minute", "minutes"),
    HOUR(60 * 60 * 1000, "hour", "h", "hrs", "hour", "hours"),
    DAY(24 * 60 * 60 * 1000, "day", "d", "day", "days");

    private final long millis;
    private final String name;
    private final String[] values;

    TimeFrame(long millis, String name, String... value) {
        this.millis = millis;
        this.name = name;
        this.values = value;
    }

    public long getMilliseconds() {
        return millis;
    }

    public String getName() {
        return name;
    }

    public static TimeFrame match(String value) {
        for (TimeFrame frame : values()) {
            for (String s : frame.values) {
                if (value.equalsIgnoreCase(s)) {
                    return frame;
                }
            }
        }
        return null;
    }

}
