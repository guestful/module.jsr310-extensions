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

import java.io.Serializable;
import java.time.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ZonedInterval implements Serializable {

    public static final ZonedInterval EMPTY = Jsr310StaticExtensions.EMPTY_ZonedInterval;

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -6260982410461394882L;

    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final ZoneId zoneId;

    private ZonedInterval(ZonedDateTime start, ZonedDateTime end) {
        this.zoneId = start.getZone();
        this.start = start;
        this.end = end.withZoneSameInstant(zoneId);
    }

    public ZoneId getZone() {
        return zoneId;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public long getStartMillis() {
        return start.toInstant().toEpochMilli();
    }

    public long getEndMillis() {
        return end.toInstant().toEpochMilli();
    }

    /**
     * Output a string in ISO8601 interval format.
     * <p>
     * From version 2.1, the string includes the time zone offset.
     *
     * @return re-parsable string (in the default zone)
     */
    @Override
    public String toString() {
        return start.toOffsetDateTime().toString() + "/" + end.toOffsetDateTime().toString();
    }

    public ZonedInterval overlap(ZonedInterval zonedInterval) {
        if (!overlaps(zonedInterval)) return null;
        long start = Math.max(getStartMillis(), zonedInterval.getStartMillis());
        long end = Math.min(getEndMillis(), zonedInterval.getEndMillis());
        return ZonedInterval.of(start, end, getZone());
    }

    /**
     * Does this time interval overlap the specified time interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * An interval overlaps another if it shares some common part of the
     * datetime continuum.
     * <p>
     * When two intervals are compared the result is one of three states:
     * (a) they abut, (b) there is a gap between them, (c) they overlap.
     * The abuts state takes precedence over the other two, thus a zero duration
     * interval at the start of a larger interval abuts and does not overlap.
     * <p>
     * For example:
     * <pre>
     * [09:00 to 10:00) overlaps [08:00 to 08:30)  = false (completely before)
     * [09:00 to 10:00) overlaps [08:00 to 09:00)  = false (abuts before)
     * [09:00 to 10:00) overlaps [08:00 to 09:30)  = true
     * [09:00 to 10:00) overlaps [08:00 to 10:00)  = true
     * [09:00 to 10:00) overlaps [08:00 to 11:00)  = true
     *
     * [09:00 to 10:00) overlaps [09:00 to 09:00)  = false (abuts before)
     * [09:00 to 10:00) overlaps [09:00 to 09:30)  = true
     * [09:00 to 10:00) overlaps [09:00 to 10:00)  = true
     * [09:00 to 10:00) overlaps [09:00 to 11:00)  = true
     *
     * [09:00 to 10:00) overlaps [09:30 to 09:30)  = true
     * [09:00 to 10:00) overlaps [09:30 to 10:00)  = true
     * [09:00 to 10:00) overlaps [09:30 to 11:00)  = true
     *
     * [09:00 to 10:00) overlaps [10:00 to 10:00)  = false (abuts after)
     * [09:00 to 10:00) overlaps [10:00 to 11:00)  = false (abuts after)
     *
     * [09:00 to 10:00) overlaps [10:30 to 11:00)  = false (completely after)
     *
     * [14:00 to 14:00) overlaps [14:00 to 14:00)  = false (abuts before and after)
     * [14:00 to 14:00) overlaps [13:00 to 15:00)  = true
     * </pre>
     *
     * @param zonedInterval the time interval to compare to, null means a zero length interval now
     * @return true if the time intervals overlap
     */
    public boolean overlaps(ZonedInterval zonedInterval) {
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        long otherStart = zonedInterval.getStartMillis();
        long otherEnd = zonedInterval.getEndMillis();
        return thisStart < otherEnd && otherStart < thisEnd;
    }

    /**
     * Gets the gap between this interval and another interval.
     * The other interval can be either before or after this interval.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * An interval has a gap to another interval if there is a non-zero
     * duration between them. This method returns the amount of the gap only
     * if the intervals do actually have a gap between them.
     * If the intervals overlap or abut, then null is returned.
     * <p>
     * When two intervals are compared the result is one of three states:
     * (a) they abut, (b) there is a gap between them, (c) they overlap.
     * The abuts state takes precedence over the other two, thus a zero duration
     * interval at the start of a larger interval abuts and does not overlap.
     * <p>
     * The chronology of the returned interval is the same as that of
     * this interval (the chronology of the interval parameter is not used).
     * Note that the use of the chronology was only correctly implemented
     * in version 1.3.
     *
     * @param zonedInterval the interval to examine, null means now
     * @return the gap interval, null if no gap
     * @since 1.1
     */
    public ZonedInterval gap(ZonedInterval zonedInterval) {
        long otherStart = zonedInterval.getStartMillis();
        long otherEnd = zonedInterval.getEndMillis();
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        if (thisStart > otherEnd) {
            return ZonedInterval.of(otherEnd, thisStart, getZone());
        } else if (otherStart > thisEnd) {
            return ZonedInterval.of(thisEnd, otherStart, getZone());
        } else {
            return null;
        }
    }

    /**
     * Does this interval abut with the interval specified.
     * <p>
     * Intervals are inclusive of the start instant and exclusive of the end.
     * An interval abuts if it starts immediately after, or ends immediately
     * before this interval without overlap.
     * A zero duration interval abuts with itself.
     * <p>
     * When two intervals are compared the result is one of three states:
     * (a) they abut, (b) there is a gap between them, (c) they overlap.
     * The abuts state takes precedence over the other two, thus a zero duration
     * interval at the start of a larger interval abuts and does not overlap.
     * <p>
     * For example:
     * <pre>
     * [09:00 to 10:00) abuts [08:00 to 08:30)  = false (completely before)
     * [09:00 to 10:00) abuts [08:00 to 09:00)  = true
     * [09:00 to 10:00) abuts [08:00 to 09:01)  = false (overlaps)
     *
     * [09:00 to 10:00) abuts [09:00 to 09:00)  = true
     * [09:00 to 10:00) abuts [09:00 to 09:01)  = false (overlaps)
     *
     * [09:00 to 10:00) abuts [10:00 to 10:00)  = true
     * [09:00 to 10:00) abuts [10:00 to 10:30)  = true
     *
     * [09:00 to 10:00) abuts [10:30 to 11:00)  = false (completely after)
     *
     * [14:00 to 14:00) abuts [14:00 to 14:00)  = true
     * [14:00 to 14:00) abuts [14:00 to 15:00)  = true
     * [14:00 to 14:00) abuts [13:00 to 14:00)  = true
     * </pre>
     *
     * @param zonedInterval the interval to examine, null means now
     * @return true if the interval abuts
     * @since 1.1
     */
    public boolean abuts(ZonedInterval zonedInterval) {
        return zonedInterval.getEndMillis() == getStartMillis() || getEndMillis() == zonedInterval.getStartMillis();
    }

    /**
     * Does this time interval contain the specified instant.
     * <p>
     * Non-zero duration intervals are inclusive of the start instant and
     * exclusive of the end. A zero duration interval cannot contain anything.
     * <p>
     * For example:
     * <pre>
     * [09:00 to 10:00) contains 08:59  = false (before start)
     * [09:00 to 10:00) contains 09:00  = true
     * [09:00 to 10:00) contains 09:59  = true
     * [09:00 to 10:00) contains 10:00  = false (equals end)
     * [09:00 to 10:00) contains 10:01  = false (after end)
     *
     * [14:00 to 14:00) contains 14:00  = false (zero duration contains nothing)
     * </pre>
     *
     * @param instant the instant, null means now
     * @return true if this time interval contains the instant
     */
    public boolean contains(ZonedDateTime instant) {
        return contains(instant.toInstant());
    }

    public boolean contains(Instant instant) {
        return contains(instant.toEpochMilli());
    }

    /**
     * Does this time interval contain the specified millisecond instant.
     * <p>
     * Non-zero duration intervals are inclusive of the start instant and
     * exclusive of the end. A zero duration interval cannot contain anything.
     *
     * @param millisInstant the instant to compare to,
     *                      millisecond instant from 1970-01-01T00:00:00Z
     * @return true if this time interval contains the millisecond
     */
    public boolean contains(long millisInstant) {
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        return millisInstant >= thisStart && millisInstant < thisEnd;
    }

    /**
     * Does this time interval contain the specified time interval.
     * <p>
     * Non-zero duration intervals are inclusive of the start instant and
     * exclusive of the end. The other interval is contained if this interval
     * wholly contains, starts, finishes or equals it.
     * A zero duration interval cannot contain anything.
     * <p>
     * When two intervals are compared the result is one of three states:
     * (a) they abut, (b) there is a gap between them, (c) they overlap.
     * The <code>contains</code> method is not related to these states.
     * In particular, a zero duration interval is contained at the start of
     * a larger interval, but does not overlap (it abuts instead).
     * <p>
     * For example:
     * <pre>
     * [09:00 to 10:00) contains [09:00 to 10:00)  = true
     * [09:00 to 10:00) contains [09:00 to 09:30)  = true
     * [09:00 to 10:00) contains [09:30 to 10:00)  = true
     * [09:00 to 10:00) contains [09:15 to 09:45)  = true
     * [09:00 to 10:00) contains [09:00 to 09:00)  = true
     *
     * [09:00 to 10:00) contains [08:59 to 10:00)  = false (otherStart before thisStart)
     * [09:00 to 10:00) contains [09:00 to 10:01)  = false (otherEnd after thisEnd)
     * [09:00 to 10:00) contains [10:00 to 10:00)  = false (otherStart equals thisEnd)
     *
     * [14:00 to 14:00) contains [14:00 to 14:00)  = false (zero duration contains nothing)
     * </pre>
     *
     * @param zonedInterval the time interval to compare to, null means a zero duration interval now
     * @return true if this time interval contains the time interval
     */
    public boolean contains(ZonedInterval zonedInterval) {
        long otherStart = zonedInterval.getStartMillis();
        long otherEnd = zonedInterval.getEndMillis();
        long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        return thisStart <= otherStart && otherStart < thisEnd && otherEnd <= thisEnd;
    }

    /**
     * Gets the duration of this time interval in milliseconds.
     * <p>
     * The duration is equal to the end millis minus the start millis.
     *
     * @return the duration of the time interval in milliseconds
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public long toDurationMillis() {
        return safeAdd(getEndMillis(), -getStartMillis());
    }

    /**
     * Gets the duration of this time interval.
     * <p>
     * The duration is equal to the end millis minus the start millis.
     *
     * @return the duration of the time interval
     * @throws ArithmeticException if the duration exceeds the capacity of a long
     */
    public Duration toDuration() {
        long durMillis = toDurationMillis();
        if (durMillis == 0) {
            return Duration.ZERO;
        } else {
            return Duration.ofMillis(durMillis);
        }
    }

    public ZonedInterval withZone(ZoneId zone) {
        if (this.zoneId.equals(zone)) return this;
        return ZonedInterval.of(start.withZoneSameInstant(zone), end.withZoneSameInstant(zone));
    }

    public ZonedInterval enlarge(Period p) {
        return ZonedInterval.of(start.minus(p), end.plus(p));
    }

    public ZonedInterval enlarge(Duration d) {
        return ZonedInterval.of(start.minus(d), end.plus(d));
    }

    /**
     * Add two values throwing an exception if overflow occurs.
     *
     * @param val1 the first value
     * @param val2 the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    private static long safeAdd(long val1, long val2) {
        long sum = val1 + val2;
        // If there is a sign change, but the two values have the same sign...
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " + " + val2);
        }
        return sum;
    }

    public static ZonedInterval of(long start, long end, ZoneId zoneId) {
        return of(Instant.ofEpochMilli(start), Instant.ofEpochMilli(end), zoneId);
    }

    public static ZonedInterval of(Instant start, Instant end, ZoneId zoneId) {
        return of(ZonedDateTime.ofInstant(start, zoneId), ZonedDateTime.ofInstant(end, zoneId));
    }

    public static ZonedInterval of(LocalDate start, LocalDate end, ZoneId zoneId) {
        return of(start.atStartOfDay(zoneId), end.atStartOfDay(zoneId));
    }

    public static ZonedInterval of(ZonedDateTime start, ZonedDateTime end) {
        return new ZonedInterval(start, end);
    }

}
