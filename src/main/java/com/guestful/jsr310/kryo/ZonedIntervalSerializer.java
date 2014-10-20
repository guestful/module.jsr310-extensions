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
package com.guestful.jsr310.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.guestful.jsr310.ZonedInterval;

import java.time.ZoneId;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ZonedIntervalSerializer extends Serializer<ZonedInterval> {

    public ZonedIntervalSerializer() {
        setImmutable(true);
    }

    @Override
    public void write(Kryo kryo, Output output, ZonedInterval object) {
        output.writeLong(object.getStartMillis(), true);
        output.writeLong(object.getEndMillis(), true);
        output.writeString(object.getZone().getId());
    }

    @Override
    public ZonedInterval read(Kryo kryo, Input input, Class<ZonedInterval> type) {
        long start = input.readLong(true);
        long end = input.readLong(true);
        String tz = input.readString();
        return ZonedInterval.of(start, end, ZoneId.of(tz));
    }
}
