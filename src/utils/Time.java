package utils;

import java.io.Serializable;

/**
 * Time checker
 */
public class Time implements Serializable, Comparable<Time> {
    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        if ((hour < 0 || hour > 23) || (minute < 0 || minute > 59)) {
            throw new IllegalArgumentException("Orario inserito errato");
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Return human like time
     *
     * @return
     */
    public String getHumanTime() {
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0" + hour);
        } else {
            sb.append(hour);
        }
        sb.append(":");
        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append(minute);
        }
        return sb.toString();
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public static String[] getHours() {
        String[] result = new String[24];
        for (int i = 0; i < 24; i++) {
            result[i] = "" + i;
        }
        return result;
    }

    public static String[] getMinutes() {
        String[] result = new String[60];
        for (int i = 0; i < 60; i++) {
            result[i] = "" + i;
        }
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;

        Time time = (Time) o;

        if (hour != time.hour) return false;
        if (minute != time.minute) return false;
        return true;
    }

    @Override
    public int compareTo(Time o) {
        int myInx = Integer.parseInt("" + hour + "" + minute);
        int otherInx = Integer.parseInt("" + o.getHour() + "" + o.getMinute());
        if (myInx == otherInx) {
            return 0;
        } else return myInx < otherInx ? -1 : 1;
    }
}
