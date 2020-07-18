package com.shanebeestudios.bot.util;

public enum TimeFrame {

    SECOND(1000, "second", "s", "sec", "secs", "second", "seconds"),
    MINUTE(60 * 1000, "minute", "m", "min", "mins", "minute", "minutes"),
    HOUR(60 * 60 * 1000, "hour", "h", "hr", "hrs", "hour", "hours"),
    DAY(24 * 60 * 60 * 1000, "day", "d", "day", "days");

    private final long millis;
    private final String name;
    private final String[] values;

    TimeFrame(long millis, String name, String... values) {
        this.millis = millis;
        this.name = name;
        this.values = values;
    }

    public long getMilliseconds() {
        return millis;
    }

    public String getName() {
        return name;
    }

    public String getFullTime(long t) {
        return t + " " + name + (t > 1 ? "s" : "");
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
