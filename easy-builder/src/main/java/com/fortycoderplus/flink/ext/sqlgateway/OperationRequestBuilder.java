/*
 * (c) Copyright 2023 40CoderPlus. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortycoderplus.flink.ext.sqlgateway;

import com.fortycoderplus.flink.ext.sqlgateway.impl.BaseOperationRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.util.MemoryUnit;

/**
 * Builder for build operation execute config
 * Notice:only for endpoint: POST: /sessions/{session_handle}/statements
 */
public interface OperationRequestBuilder<SELF, EXECUTION> {

    static BaseOperationRequestBuilder builder() {
        return new BaseOperationRequestBuilder();
    }

    /**
     * @return EXECUTION. execution to submit to flink sql gateway
     */
    EXECUTION build();

    /**
     * Set Statement Pipeline Name
     *
     * @param pipelineName Flink Pipeline Name
     * @return SELF OperationRequestBuilder
     */
    default SELF pipelineName(String pipelineName) {
        return executeConfig("pipeline.name", pipelineName);
    }

    /**
     * The default parallelism used when no parallelism is specified anywhere (default: 1).
     *
     * @param parallelism number of parallelism
     * @return SELF OperationRequestBuilder
     */
    default SELF parallelism(int parallelism) {
        return executeConfig("parallelism.default", String.valueOf(parallelism));
    }

    /**
     * The number of slots that a TaskManager offers
     * @param numberOfTaskSlots The number of slots that a TaskManager offers
     * @return SELF OperationRequestBuilder
     */
    default SELF numberOfTaskSlots(int numberOfTaskSlots) {
        return executeConfig("taskmanager.numberOfTaskSlots", String.valueOf(numberOfTaskSlots));
    }

    /**
     * Total Process Memory size for the TaskExecutors
     * @param number number of memory
     * @param unit unit of memory
     * @return SELF OperationRequestBuilder
     */
    default SELF taskmanagerMemoryProcessSize(int number, MemoryUnit unit) {
        return executeConfig("taskmanager.memory.process.size", String.format("%d %s", number, unit.getUnits()[1]));
    }

    /**
     * Total Flink Memory size for the TaskExecutors.
     * @param number number of memory
     * @param unit unit of memory
     * @return SELF OperationRequestBuilder
     */
    default SELF taskmanagerMemoryFlinkSize(int number, MemoryUnit unit) {
        return executeConfig("taskmanager.memory.flink.size", String.format("%d %s", number, unit.getUnits()[1]));
    }

    /**
     * SQL Statement to submit to Flink Cluster
     *
     * @param statement Flink SQL statement
     * @return SELF return builder
     */
    SELF statement(String statement);

    /**
     * Set Execution Timeout
     *
     * @param timeout execution timeout
     * @return SELF return builder
     */
    SELF timeout(Long timeout);

    /**
     * Streaming Mode
     *
     * @return SELF return builder
     */
    default SELF streaming() {
        return executeConfig("execution.runtime-mode", "STREAMING");
    }

    /**
     * Batch Mode
     *
     * @return SELF return builder
     */
    default SELF batch() {
        return executeConfig("execution.runtime-mode", "BATCH");
    }

    /**
     * Add a special config to Statement Execute Config
     *
     * @param key Execute Config Key
     * @param value Execute Config Value
     * @return SELF OperationRequestBuilder
     */
    SELF executeConfig(String key, String value);
}
