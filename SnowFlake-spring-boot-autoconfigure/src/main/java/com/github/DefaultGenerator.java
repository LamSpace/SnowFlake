/**
 * Copyright 2020 the original author, Lin Tang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github;

import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of {@link com.github.Generator}. <code>com.github.DefaultGenerator</code>
 * generates a distinct id directly.
 *
 * @author Lin Tang
 */
public class DefaultGenerator implements com.github.Generator {

    private final StampedLock lock = new StampedLock();

    private final SnowFlakeConfiguration configuration;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    public DefaultGenerator(SnowFlakeConfiguration configuration) {
        Logger logger = Logger.getLogger("com.github.generator");
        logger.setLevel(Level.INFO);
        logger.info("Initialized Default Generator for SnowFlake.");
        this.configuration = configuration;
    }

    public long nextId() {
        long stamp = lock.writeLock(), ans;
        try {
            long timestamp = System.currentTimeMillis();
            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Current time is smaller than last timestamp");
            }
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & configuration.getSEQUENCE_MASK();
                if (sequence == 0) {
                    timestamp = tillNextMills(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            ans = ((timestamp - configuration.getINITIAL_TIME_STAMP()) << configuration.getTIME_STAMP_OFFSET()) |
                    (configuration.getDataCenterId() << configuration.getDATA_CENTER_ID_OFFSET()) |
                    (configuration.getWorkerId() << configuration.getWORKER_ID_OFFSET()) | sequence;
        } finally {
            lock.unlockWrite(stamp);
        }
        return ans;
    }

    private long tillNextMills(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
