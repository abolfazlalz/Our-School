package com.futech.our_school.utils.request;

public class TimeData {

    private int hours;
    private int minutes;
    private int seconds;

    public TimeData(String time) {
        String[] times = time.split(":");
        if (times.length > 0) hours = Integer.parseInt(times[0]);
        if (times.length > 1) minutes = Integer.parseInt(times[1]);
        if (times.length > 2) seconds = Integer.parseInt(times[2]);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public String format(String pattern) {
        return pattern.
                replace("h", String.valueOf(getHours())).
                replace("m", String.valueOf(getMinutes())).
                replace("s", String.valueOf(getMinutes()));
    }
}
