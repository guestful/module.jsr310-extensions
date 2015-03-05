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

import com.guestful.json.groovy.GroovyJsonSerializer;
import com.guestful.jsr310.ZonedInterval;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class GroovyJsr310 {
    public static void addJsr310EncodingHook(GroovyJsonSerializer serializer) {
        serializer.addCustomSerializer(ZonedInterval.class, (o, writer) -> {
            Map<String, ZonedDateTime> map = new LinkedHashMap<>();
            map.put("start", o.getStart());
            map.put("end", o.getEnd());
            writer.writeMap(map);
        });
    }
}
