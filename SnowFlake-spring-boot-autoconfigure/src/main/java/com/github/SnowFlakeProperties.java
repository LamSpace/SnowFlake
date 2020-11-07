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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Basic properties for <code>SnowFlake</code>
 *
 * @author Lin Tang
 */
@ConfigurationProperties(prefix = SnowFlakeProperties.SNOWFLAKE_PREFIX)
public class SnowFlakeProperties {

    /**
     * Prefix of SnowFlake properties.
     */
    public static final String SNOWFLAKE_PREFIX = "snowflake";


    /**
     * Initial time stamp, usually the time stamp when the application is created.
     * For example,
     * <pre>
     *     Calendar calendar = Calendar.getInstance();
     *     calendar.set(2020, Calendar.NOVEMBER, 1);
     *     long timestamp = calendar.getTimeInMillis());
     * </pre>
     */
    private long initialTimestamp = 1604383611644L;

    /**
     * # of bits for worker id.
     */
    private long workerIdBits = 5L;

    /**
     * # of bits for data center.
     */
    private long dataCenterIdBits = 5L;

    /**
     * current worker id.
     */
    private long workerId = 1L;

    /**
     * current data center id.
     */
    private long dataCenterId = 1L;

    public long getInitialTimestamp() {
        return initialTimestamp;
    }

    public void setInitialTimestamp(long initialTimestamp) {
        this.initialTimestamp = initialTimestamp;
    }

    public long getWorkerIdBits() {
        return workerIdBits;
    }

    public void setWorkerIdBits(long workerIdBits) {
        this.workerIdBits = workerIdBits;
    }

    public long getDataCenterIdBits() {
        return dataCenterIdBits;
    }

    public void setDataCenterIdBits(long dataCenterIdBits) {
        this.dataCenterIdBits = dataCenterIdBits;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

}
