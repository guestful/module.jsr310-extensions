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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FixedClock extends Clock {

    private ZoneId zone = ZoneId.systemDefault();
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer min;
    private Integer sec;

    public FixedClock() {
    }

    public FixedClock(FixedClock o) {
        this.zone = o.zone;
        this.year = o.year;
        this.month = o.month;
        this.day = o.day;
        this.hour = o.hour;
        this.min = o.min;
        this.sec = o.sec;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }

    public void setZoneId(String zone) {
        this.zone = ZoneId.of(zone);
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        FixedClock clock = new FixedClock(this);
        clock.setZone(zone);
        return clock;
    }

    @Override
    public Instant instant() {
        ZonedDateTime dt = ZonedDateTime.now(zone);
        if (year != null) dt = dt.withYear(year);
        if (month != null) dt = dt.withMonth(month);
        if (day != null) dt = dt.withDayOfMonth(day);
        if (hour != null) dt = dt.withHour(hour);
        if (min != null) dt = dt.withMinute(min);
        if (sec != null) dt = dt.withSecond(sec);
        return dt.toInstant();
    }

}
