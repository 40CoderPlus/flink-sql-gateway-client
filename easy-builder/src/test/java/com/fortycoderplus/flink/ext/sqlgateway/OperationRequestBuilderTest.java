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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fortycoderplus.flink.ext.sqlgateway.impl.BaseOperationRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.model.ExecuteStatementRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationRequestBuilderTest {

    BaseOperationRequestBuilder builder;

    @BeforeEach
    void setUp() {
        builder = OperationRequestBuilder.builder();
    }

    @Test
    void statement() {
        builder.statement("select 1");
        assertEquals("select 1", builder.build().getStatement());
    }

    @Test
    void timeout() {
        builder.timeout(1L);
        assertEquals(1L, builder.build().getExecutionTimeout());
    }

    @Test
    void streaming() {
        builder.streaming();
        assertEquals("STREAMING", builder.build().getExecutionConfig().get("execution.runtime-mode"));
    }

    @Test
    void batch() {
        builder.streaming();
        assertEquals("STREAMING", builder.build().getExecutionConfig().get("execution.runtime-mode"));
        ExecuteStatementRequestBody body = builder.streaming()
                .maxConcurrentCheckpoints(1)
                .storage()
                .backend()
                .noExternalizedCheckpoints()
                .build();
        assertEquals(5, body.getExecutionConfig().size());
    }

    @Test
    void pipelineName() {
        builder.pipelineName("test1");
        assertEquals("test1", builder.build().getExecutionConfig().get("pipeline.name"));
        builder.pipelineName("test2");
        assertEquals("test2", builder.build().getExecutionConfig().get("pipeline.name"));
    }

    @Test
    void executeConfig() {
        assertEquals(
                1,
                builder.executeConfig("key1", "key2")
                        .build()
                        .getExecutionConfig()
                        .size());
    }

    @Test
    void statementSet() {
        builder.statementSet("select 1", "select 2");
        assertEquals(
                "EXECUTE STATEMENT SET" + System.lineSeparator() + "BEGIN" + System.lineSeparator() + "select 1;"
                        + System.lineSeparator() + "select 2" + System.lineSeparator() + "END",
                builder.build().getStatement());
    }
}
