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
package com.guestful.jsr310.groovy;

import com.guestful.json.groovy.CustomizableJsonOutput;
import com.guestful.jsr310.Jsr310Extensions;
import com.guestful.jsr310.ZonedInterval;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.temporal.ChronoField.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class GroovyJsr310 {

    private static final DateTimeFormatter ISO_OFFSET_DATE_TIME = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE)
        .appendLiteral('T')
        .appendValue(HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(MINUTE_OF_HOUR, 2)
        .appendLiteral(':')
        .appendValue(SECOND_OF_MINUTE, 2)
        .appendFraction(MILLI_OF_SECOND, 3, 3, true)
        .appendOffsetId()
        .toFormatter();

    public static void addJsr310EncodingHook(CustomizableJsonOutput output) {
        output.addHookString(LocalTime.class)
            .addHook(DayOfWeek.class, (o, buffer, outputer) -> buffer.addQuoted(Jsr310Extensions.getShortName(o)))
            .addHook(Month.class, (o, buffer, outputer) -> buffer.addQuoted(Jsr310Extensions.getShortName(o)))
            .addHook(ZoneId.class, (o, buffer, outputer) -> buffer.addQuoted(o.getId()))
            .addHook(LocalDate.class, (o, buffer, outputer) -> buffer.addQuoted(o.toString()))
            .addHook(LocalTime.class, (o, buffer, outputer) -> buffer.addQuoted(o.toString()))
            .addHook(OffsetDateTime.class, (o, buffer, outputer) -> buffer.addQuoted(o.format(ISO_OFFSET_DATE_TIME)))
            .addHook(ZonedDateTime.class, (o, buffer, outputer) -> buffer.addQuoted(o.toOffsetDateTime().format(ISO_OFFSET_DATE_TIME)))
            .addHook(Duration.class, (o, buffer, outputer) -> outputer.writeNumber(Long.class, o.toMillis(), buffer))
            .addHook(Period.class, (o, buffer, outputer) -> outputer.writeNumber(Long.class, Jsr310Extensions.toDuration(o).toMillis(), buffer))
            .addHook(ZonedInterval.class, (o, buffer, outputer) -> {
                Map<String, ZonedDateTime> map = new LinkedHashMap<>();
                map.put("start", o.getStart());
                map.put("end", o.getEnd());
                outputer.writeMap(map, buffer);
            });
    }

}
