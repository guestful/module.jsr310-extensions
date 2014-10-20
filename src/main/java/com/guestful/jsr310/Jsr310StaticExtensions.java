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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Jsr310StaticExtensions {

    // maximum dates. millisecond is: 253402235999000 (9999-12-31T23:59:59+18:00)
    private static final ZonedDateTime MAX_ZonedDateTime = ZonedDateTime.of(9999, 12, 31, 23, 59, 59, 0, ZoneOffset.MAX);
    static final ZonedInterval EMPTY_ZonedInterval = ZonedInterval.of(MAX_ZonedDateTime, MAX_ZonedDateTime);
    private static final List<DayOfWeek> ALL_DayOfWeek = Collections.unmodifiableList(Arrays.asList(DayOfWeek.values()));
    private static final List<Month> ALL_Month = Collections.unmodifiableList(Arrays.asList(Month.values()));

    public static ZonedDateTime getMAX(ZonedDateTime o) {
        return MAX_ZonedDateTime;
    }

    public static ZonedDateTime of(ZonedDateTime o, int year, int month, int dayOfMonth, int hour, int minute) {
        return ZonedDateTime.of(year, month, dayOfMonth, hour, minute, 0, 0, ZoneId.systemDefault());
    }

    public static List<DayOfWeek> getALL(DayOfWeek o) {
        return ALL_DayOfWeek;
    }

    public static List<Month> getALL(Month o) {
        return ALL_Month;
    }

    public static Month fromName(Month o, String name) {
        for (Month month : Month.values()) {
            if (month.name().startsWith(name)) {
                return month;
            }
        }
        throw new IllegalArgumentException("No enum constant starting by java.time.Month." + name);
    }

    public static DayOfWeek fromName(DayOfWeek o, String name) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().startsWith(name)) {
                return day;
            }
        }
        throw new IllegalArgumentException("No enum constant starting by java.time.DayOfWeek." + name);
    }

}
