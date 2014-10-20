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
package com.guestful.jsr310.mongo;

import com.guestful.jsr310.Jsr310Extensions;
import com.guestful.jsr310.ZonedInterval;
import org.bson.BSON;

import java.time.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MongoJsr310 {

    private static boolean hooked;

    public static void addJsr310EncodingHook() {
        if (!hooked) {
            hook(Instant.class, Date::from);
            hook(DayOfWeek.class, Jsr310Extensions::getShortName);
            hook(Month.class, Jsr310Extensions::getShortName);
            hook(ZonedDateTime.class, o -> Date.from(o.toInstant()));
            hook(LocalTime.class, LocalTime::toString);
            hook(LocalDate.class, LocalDate::toString);
            hook(ZoneId.class, ZoneId::getId);
            hook(Period.class, o -> Jsr310Extensions.toDuration(o).toMillis());
            hook(Duration.class, Duration::toMillis);
            hook(ZonedInterval.class, o -> {
                Map<String, ZonedDateTime> map = new LinkedHashMap<>();
                map.put("start", o.getStart());
                map.put("end", o.getEnd());
                return map;
            });
            hooked = true;
        }
    }

    private static <T> void hook(Class<T> type, Function<T, Object> fn) {
        BSON.addEncodingHook(type, o -> type.isInstance(o) ? fn.apply(type.cast(o)) : o);
    }

}
