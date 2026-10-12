package zest.springbackend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return hour * 60 + minute;
    }

    public static String minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format(Locale.US, "%02d:%02d", hours, mins);
    }

    public static DayOfWeek dateToDayOfWeek(String isoDate) {
        return LocalDate.parse(isoDate).getDayOfWeek();
    }

    public static String nowUTC() {
        return ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toString();
    }

    public static boolean slotsOverlap(String dayOfWeek, String startTime, String endTime,
                                      String otherDay, String otherStart, String otherEnd) {
        if (!dayOfWeek.equals(otherDay)) {
            return false;
        }
        return timeToMinutes(startTime) < timeToMinutes(otherEnd)
            && timeToMinutes(endTime) > timeToMinutes(otherStart);
    }

    public static String formatDate(String isoDate) {
        LocalDate date = LocalDate.parse(isoDate);
        return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
    }

    public static String utcToLocalTime(String utcTime, String dateStr, String zoneId) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(utcTime);
        ZonedDateTime utc = ZonedDateTime.of(date, time, ZoneId.of("UTC"));
        return utc.withZoneSameInstant(ZoneId.of(zoneId)).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
