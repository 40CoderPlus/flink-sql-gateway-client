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

/**
 * Builder for build operation execute config
 * Notice:only for endpoint: POST: /sessions/{session_handle}/statements
 */
public interface OperationRequestBuilder<SELF, EXECUTION> {

    static BaseOperationRequestBuilder builder() {
        return new BaseOperationRequestBuilder();
    }

    /**
     *
     * @return EXECUTION. execution to submit to flink sql gateway
     */
    EXECUTION build();

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
     * Set Statement Pipeline Name
     *
     * @param pipelineName Flink Pipeline Name
     * @return SELF OperationRequestBuilder
     */
    default SELF pipelineName(String pipelineName) {
        return executeConfig("pipeline.name", pipelineName);
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
