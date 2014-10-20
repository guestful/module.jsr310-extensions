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

import java.time.LocalTime;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class LocalTimeSerializer extends Serializer<LocalTime> {

    public LocalTimeSerializer() {
        setImmutable(true);
    }

    @Override
    public void write(Kryo kryo, Output output, LocalTime object) {
        output.writeInt(object.getHour(), true);
        output.writeInt(object.getMinute(), true);
        output.writeInt(object.getSecond(), true);
        output.writeInt(object.getNano(), true);
    }

    @Override
    public LocalTime read(Kryo kryo, Input input, Class<LocalTime> type) {
        int hours = input.readInt(true);
        int mins = input.readInt(true);
        int secs = input.readInt(true);
        int nanos = input.readInt(true);
        return LocalTime.of(hours, mins, secs, nanos);
    }

}
