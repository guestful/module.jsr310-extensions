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
import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DelegatingClock extends Clock {

    private Clock delegate;
    private ZoneId zoneId;

    public DelegatingClock(Clock delegate) {
        this.delegate = delegate;
    }

    private DelegatingClock(Clock delegate, ZoneId zoneId) {
        this.delegate = delegate;
        this.zoneId = zoneId;
    }

    public <V> V runWithClock(Clock clock, Callable<V> c) throws Exception {
        Clock old = delegate;
        delegate = clock;
        try {
            return c.call();
        } finally {
            delegate = old;
        }
    }

    public void runWithClock(Clock clock, Runnable r) {
        Clock old = delegate;
        delegate = clock;
        try {
            r.run();
        } finally {
            delegate = old;
        }
    }

    @Override
    public ZoneId getZone() {
        return zoneId == null ? delegate.getZone() : zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new DelegatingClock(this, zone);
    }

    @Override
    public Instant instant() {
        return delegate.instant();
    }
}
