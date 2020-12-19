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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An advanced implementation of {@link com.github.Generator}. <code>CachedGenerator</code>
 * cached the generated identities in a {@link java.util.concurrent.ConcurrentLinkedQueue}
 * with default or specified pool size in {@link com.github.SnowFlakeProperties}. This is
 * an efficient and thread-safe generator implementation.
 *
 * @author Lin Tang
 */
public class CachedGenerator implements Generator {

    private final SnowFlakeConfiguration configuration;

    private final StampedLock lock = new StampedLock();

    private final Queue<Long> queue;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    public CachedGenerator(SnowFlakeConfiguration configuration) {
        Logger logger = Logger.getLogger("con.github.generator");
        logger.setLevel(Level.INFO);
        logger.info("Initialized Cached Generator for SnowFlake.");
        this.configuration = configuration;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public long nextId() {
        Long id;
        while ((id = this.queue.poll()) == null) {
            long stamp = this.lock.writeLock();
            try {
                if (this.queue.peek() == null) {
                    for (int i = 0, size = configuration.getPoolSize(); i < size; i++) {
                        generateIds();
                    }
                }
            } finally {
                this.lock.unlockWrite(stamp);
            }
        }
        return id;
    }

    private void generateIds() {
        long ans, timestamp = System.currentTimeMillis();
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
        queue.offer(ans);
    }

    private long tillNextMills(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
