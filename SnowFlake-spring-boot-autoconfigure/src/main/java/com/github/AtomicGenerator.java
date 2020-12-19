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

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The most advanced implementation of {@link com.github.Generator}, using <code>Atomic</code>
 * operations to generate a global unique identity. This kind of implementation is totally
 * <code>Lock Free</code> by using <code>Compare and Swap (CAS)</code> and <code>Spin</code>
 * operations than explicit lock like {@link java.util.concurrent.locks.StampedLock}. This kind
 * of <code>Generator</code> is the most efficient one, and thread-safe obviously.
 *
 * @author Lin Tang
 */
public class AtomicGenerator implements Generator {

    private final SnowFlakeConfiguration configuration;

    private final long CONSTANT_DATA_CENTER;

    private final long CONSTANT_WORKER_ID;

    private AtomicLong sequence = new AtomicLong(0L);

    private AtomicLong lastTimestamp = new AtomicLong(-1L);

    public AtomicGenerator(SnowFlakeConfiguration configuration) {
        Logger logger = Logger.getLogger("con.github.generator");
        logger.log(Level.INFO, "Initialized Atomic Generator for SnowFlake.");
        this.configuration = configuration;
        this.CONSTANT_DATA_CENTER = configuration.getDataCenterId() << configuration.getDATA_CENTER_ID_OFFSET();
        this.CONSTANT_WORKER_ID = configuration.getWorkerId() << configuration.getWORKER_ID_OFFSET();
    }

    @Override
    public long nextId() {
        long newTimestamp, oldTimestamp, oldSequence, newSequence;
        do {
            oldTimestamp = lastTimestamp.get();
            oldSequence = sequence.get();
            newTimestamp = System.currentTimeMillis();
            if (newTimestamp < oldTimestamp) {
                throw new RuntimeException("Current time is smaller than last timestamp");
            }
            if (newTimestamp == oldTimestamp) {
                newSequence = (oldSequence + 1L) & configuration.getSEQUENCE_MASK();
                if (newSequence == 0L) {
                    newTimestamp = nextMills(oldTimestamp);
                }
            } else {
                newSequence = 0L;
            }
            // As you can see, there are two AtomicLong type fields. Hence if any of them, or both of them, fail to set
            // to the new value using method compareAndSet(long expected, long newVal), it means other thread(s) changed
            // old value of both AtomicLong type filed.
            //
            // So the only way to exit the do-loop is that both of AtomicLong.compareAndSet(long expected, long newValue) return true.
            //
            // '||' here means if any of both returns false, just try again to generate the identity.
        } while (!lastTimestamp.compareAndSet(oldTimestamp, newTimestamp) || !sequence.compareAndSet(oldSequence, newSequence));
        return ((newTimestamp - configuration.getINITIAL_TIME_STAMP()) << configuration.getTIME_STAMP_OFFSET()) |
                this.CONSTANT_DATA_CENTER | this.CONSTANT_WORKER_ID | newSequence;
    }

    private long nextMills(long lastTimestamp) {
        long timestamp;
        while ((timestamp = System.currentTimeMillis()) <= lastTimestamp) ;
        return timestamp;
    }

}
