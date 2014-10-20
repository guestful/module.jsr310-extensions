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

import java.time.LocalDate;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class LocalDateSerializer extends Serializer<LocalDate> {

    public LocalDateSerializer() {
        setImmutable(true);
    }

    @Override
    public void write(Kryo kryo, Output output, LocalDate object) {
        output.writeInt(object.getYear(), true);
        output.writeInt(object.getMonthValue(), true);
        output.writeInt(object.getDayOfMonth(), true);
    }

    @Override
    public LocalDate read(Kryo kryo, Input input, Class<LocalDate> type) {
        int year = input.readInt(true);
        int month = input.readInt(true);
        int day = input.readInt(true);
        return LocalDate.of(year, month, day);
    }
}
