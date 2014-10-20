/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.jsr310;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Jsr310Extensions {

    private static final Pattern PATTERN_DATETIME = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d(:\\d\\d(\\.\\d+)?)?");
    private static final Pattern PATTERN_DATE = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
    private static final Pattern PATTERN_MONTH = Pattern.compile("\\d\\d\\d\\d-\\d\\d");

    public static ZonedDateTime getNow(Clock c) {
        return ZonedDateTime.now(c);
    }

    public static Clock withZone(Clock c, String zoneId) {
        return c.withZone(ZoneId.of(zoneId));
    }

    public static ZonedDateTime at(ZonedDateTime dt, int h) {
        return at(dt, h, 0);
    }

    public static ZonedDateTime at(ZonedDateTime dt, int h, int min) {
        return atStartOfDay(dt).withHour(h).withMinute(min);
    }

    public static ZonedDateTime at(ZonedDateTime dt, LocalTime lt) {
        return atStartOfDay(dt).with(lt);
    }

    public static ZonedDateTime inParis(ZonedDateTime dt) {
        return dt.withZoneSameLocal(ZoneId.of("Europe/Paris"));
    }

    public static ZonedDateTime inMontreal(ZonedDateTime dt) {
        return dt.withZoneSameLocal(ZoneId.of("America/Montreal"));
    }

    public static Duration negative(Duration d) {
        return Duration.ofNanos(-d.toNanos());
    }

    public static Period negative(Period p) {
        return p.negated();
    }

    public static Duration getSec(Number n) {
        return getSecs(n);
    }

    public static Duration getSecs(Number n) {
        return Duration.ofSeconds(n.longValue());
    }

    public static Duration getMin(Number n) {
        return getMins(n);
    }

    public static Duration getMins(Number n) {
        return Duration.ofMinutes(n.longValue());
    }

    public static Duration getHour(Number n) {
        return getHours(n);
    }

    public static Duration getHours(Number n) {
        return Duration.ofHours(n.longValue());
    }

    public static Period getDay(Number n) {
        return getDays(n);
    }

    public static Period getDays(Number n) {
        return Period.ofDays(Math.toIntExact(n.longValue()));
    }

    public static Period getMonth(Number n) {
        return getMonths(n);
    }

    public static Period getMonths(Number n) {
        return Period.ofMonths(Math.toIntExact(n.longValue()));
    }

    public static Period getWeek(Number n) {
        return getWeeks(n);
    }

    public static Period getWeeks(Number n) {
        return Period.ofWeeks(Math.toIntExact(n.longValue()));
    }

    public static List<DayOfWeek> to(DayOfWeek start, DayOfWeek end) {
        List<DayOfWeek> days = new ArrayList<>();
        int max = end.getValue() - 1;
        int i = start.getValue() - 1;
        if (i > max) max += 7;
        for (; i <= max; i++) {
            days.add(DayOfWeek.of((i % 7) + 1));
        }
        return days;
    }

    public static List<Month> to(Month start, Month end) {
        List<Month> months = new ArrayList<>();
        int max = end.getValue() - 1;
        int i = start.getValue() - 1;
        if (i > max) max += 7;
        for (; i <= max; i++) {
            months.add(Month.of((i % 12) + 1));
        }
        return months;
    }

    public static String getShortName(DayOfWeek d) {
        return d.name().substring(0, 3);
    }

    public static String getShortName(Month m) {
        return m.name().substring(0, 3);
    }

    public static String toString(ZonedDateTime dt, String pattern) {
        return format(dt, pattern);
    }

    public static String toString(ZonedDateTime dt, String pattern, Locale locale) {
        return format(dt, pattern, locale);
    }

    public static String format(ZonedDateTime dt, String pattern) {
        return dt.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(ZonedDateTime dt, String pattern, Locale locale) {
        return dt.format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static Date toDate(ZonedDateTime dt) {
        return Date.from(dt.toInstant());
    }

    public static ZonedDateTime toZonedDateTime(Clock c, Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), c.getZone());
    }

    public static ZonedDateTime toZonedDateTime(Clock c, Temporal t) {
        return ZonedDateTime.class.isAssignableFrom(t.getClass()) ? ZonedDateTime.class.cast(t) : parse(c, t.toString());
    }

    public static ZonedDateTime parse(Clock c, String str) {
        if (PATTERN_DATETIME.matcher(str).matches()) return LocalDateTime.parse(str).atZone(c.getZone());
        if (PATTERN_DATE.matcher(str).matches()) return LocalDate.parse(str).atStartOfDay(c.getZone());
        if (PATTERN_MONTH.matcher(str).matches()) return LocalDate.parse(str + "-01").atStartOfDay(c.getZone());
        return OffsetDateTime.parse(str).atZoneSameInstant(c.getZone());
    }

    public static ZonedDateTime getNextMonday(ZonedDateTime d) {
        return next(d, DayOfWeek.MONDAY);
    }

    public static ZonedDateTime getNextTuesday(ZonedDateTime d) {
        return next(d, DayOfWeek.TUESDAY);
    }

    public static ZonedDateTime getNextWednesday(ZonedDateTime d) {
        return next(d, DayOfWeek.WEDNESDAY);
    }

    public static ZonedDateTime getNextThursday(ZonedDateTime d) {
        return next(d, DayOfWeek.THURSDAY);
    }

    public static ZonedDateTime getNextFriday(ZonedDateTime d) {
        return next(d, DayOfWeek.FRIDAY);
    }

    public static ZonedDateTime getNextSaturday(ZonedDateTime d) {
        return next(d, DayOfWeek.SATURDAY);
    }

    public static ZonedDateTime getNextSunday(ZonedDateTime d) {
        return next(d, DayOfWeek.SUNDAY);
    }

    public static ZonedDateTime next(ZonedDateTime d, DayOfWeek day) {
        while (d.getDayOfWeek() != day) {
            d = d.plusDays(1);
        }
        return d;
    }

    public static ZonedDateTime january(Number day, int year) {
        return byMonth(day, 1, year);
    }

    public static ZonedDateTime february(Number day, int year) {
        return byMonth(day, 2, year);
    }

    public static ZonedDateTime march(Number day, int year) {
        return byMonth(day, 3, year);
    }

    public static ZonedDateTime april(Number day, int year) {
        return byMonth(day, 4, year);
    }

    public static ZonedDateTime may(Number day, int year) {
        return byMonth(day, 5, year);
    }

    public static ZonedDateTime june(Number day, int year) {
        return byMonth(day, 6, year);
    }

    public static ZonedDateTime july(Number day, int year) {
        return byMonth(day, 7, year);
    }

    public static ZonedDateTime august(Number day, int year) {
        return byMonth(day, 8, year);
    }

    public static ZonedDateTime september(Number day, int year) {
        return byMonth(day, 9, year);
    }

    public static ZonedDateTime october(Number day, int year) {
        return byMonth(day, 10, year);
    }

    public static ZonedDateTime november(Number day, int year) {
        return byMonth(day, 11, year);
    }

    public static ZonedDateTime december(Number day, int year) {
        return byMonth(day, 12, year);
    }

    private static ZonedDateTime byMonth(Number day, int month, int year) {
        return ZonedDateTime.of(year, month, day.intValue(), 0, 0, 0, 0, ZoneId.systemDefault());
    }

    public static ZonedInterval to(ZonedDateTime from, ZonedDateTime to) {
        return ZonedInterval.of(from, to);
    }

    public static ZonedInterval upFrom(ZonedDateTime from, Duration d) {
        return upTo(from, negative(d));
    }

    public static ZonedInterval upFrom(ZonedDateTime from, Period p) {
        return upTo(from, negative(p));
    }

    public static ZonedInterval upTo(ZonedDateTime from, Duration d) {
        long nanos = d.toNanos();
        return nanos >= 0 ? ZonedInterval.of(from, from.plus(d)) : ZonedInterval.of(from.plus(d), from);
    }

    public static ZonedInterval upTo(ZonedDateTime from, Period p) {
        return p.isNegative() ? ZonedInterval.of(from.plus(p), from) : ZonedInterval.of(from, from.plus(p));
    }

    public static ZonedInterval roundUpMinutes(ZonedInterval i, int step) {
        ZonedDateTime start = roundUpMinutes(i.getStart(), step);
        ZonedDateTime end = roundUpMinutes(i.getEnd(), step);
        return start == i.getStart() && end == i.getEnd() ? i : ZonedInterval.of(start, end);
    }

    public static ZonedDateTime roundDownMinutes(ZonedDateTime time, int step) {
        int mins = time.getMinute();
        time = time.withSecond(0).withNano(0);
        if (mins % step == 0) return time;
        if (60 % step != 0) throw new IllegalArgumentException("Invalid step: " + step);
        return time.withMinute(mins - (mins % step));
    }

    public static ZonedDateTime roundUpMinutes(ZonedDateTime time, int step) {
        int mins = time.getMinute();
        time = time.withSecond(0).withNano(0);
        if (mins % step == 0) return time;
        if (60 % step != 0) throw new IllegalArgumentException("Invalid step: " + step);
        mins = mins + step - (mins % step);
        return mins < 60 ? time.withMinute(mins) : time.plusHours(1).withMinute(mins - 60);
    }

    public static LocalTime roundDownMinutes(LocalTime time, int step) {
        int mins = time.getMinute();
        time = time.withSecond(0).withNano(0);
        if (mins % step == 0) return time;
        if (60 % step != 0) throw new IllegalArgumentException("Invalid step: " + step);
        return time.withMinute(mins - (mins % step));
    }

    public static LocalTime roundUpMinutes(LocalTime time, int step) {
        int mins = time.getMinute();
        time = time.withSecond(0).withNano(0);
        if (mins % step == 0) return time;
        if (60 % step != 0) throw new IllegalArgumentException("Invalid step: " + step);
        mins = mins + step - (mins % step);
        return mins < 60 ? time.withMinute(mins) : time.plusHours(1).withMinute(mins - 60);
    }

    public static long intdiv(Duration a, Duration b) {
        return a.toNanos() / b.toNanos();
    }

    public static Duration multiply(Duration d, long multiplicand) {
        return d.multipliedBy(multiplicand);
    }

    public static LocalTime plus(LocalTime time, LocalTime add) {
        return time.plus(toDuration(add));
    }

    public static LocalTime minus(LocalTime time, LocalTime sub) {
        return time.minus(toDuration(sub));
    }

    public static ZonedDateTime atStartOfDay(ZonedDateTime o) {
        return o.toLocalDate().atStartOfDay(o.getZone());
    }

    public static ZonedDateTime withDayOfWeek(ZonedDateTime dt, DayOfWeek day) {
        return dt.with(day);
    }

    public static ZonedDateTime atZone(Date d, ZoneId zone) {
        return d.toInstant().atZone(zone);
    }

    public static ZonedDateTime atStartOfWeek(ZonedDateTime dt) {
        return dt.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay(dt.getZone());
    }

    public static Duration toDuration(LocalTime time) {
        return Duration.ofNanos(time.toNanoOfDay());
    }

    public static Duration toDuration(Period p) {
        if (p.getMonths() > 0) throw new IllegalArgumentException(p.toString());
        if (p.getYears() > 0) throw new IllegalArgumentException(p.toString());
        return Duration.ofDays(p.getDays());
    }

}
