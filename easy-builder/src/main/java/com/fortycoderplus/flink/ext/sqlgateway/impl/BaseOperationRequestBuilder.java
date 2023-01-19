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

package com.fortycoderplus.flink.ext.sqlgateway.impl;

import com.fortycoderplus.flink.ext.sqlgateway.OperationRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.model.ExecuteStatementRequestBody;
import com.fortycoderplus.flink.ext.sqlgateway.util.DurationFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseOperationRequestBuilder
        implements OperationRequestBuilder<BaseOperationRequestBuilder, ExecuteStatementRequestBody> {

    protected final ExecuteStatementRequestBody requestBody;

    // key for streaming state and checkpointing
    private static final String STATE_BACKEND = "state.backend";
    private static final String STATE_CHECKPOINT_STORAGE = "state.checkpoint-storage";
    private static final String STATE_CHECKPOINTS_DIR = "state.checkpoints.dir";
    private static final String EXECUTION_CHECKPOINTING_INTERVAL = "execution.checkpointing.interval";
    private static final String EXECUTION_CHECKPOINTING_MIN_PAUSE = "execution.checkpointing.min-pause";
    private static final String EXECUTION_CHECKPOINTING_TIMEOUT = "execution.checkpointing.timeout";
    private static final String EXECUTION_CHECKPOINTING_EXTERNALIZED_CHECKPOINT_RETENTION =
            "execution.checkpointing.externalized-checkpoint-retention";
    private static final String EXECUTION_CHECKPOINTING_MAX_CONCURRENT_CHECKPOINTS =
            "execution.checkpointing.max-concurrent-checkpoints";

    private static List<String> keys = new ArrayList<>() {
        {
            add(STATE_BACKEND);
            add(STATE_CHECKPOINT_STORAGE);
            add(STATE_CHECKPOINTS_DIR);
            add(EXECUTION_CHECKPOINTING_INTERVAL);
            add(EXECUTION_CHECKPOINTING_MIN_PAUSE);
            add(EXECUTION_CHECKPOINTING_TIMEOUT);
            add(EXECUTION_CHECKPOINTING_EXTERNALIZED_CHECKPOINT_RETENTION);
            add(EXECUTION_CHECKPOINTING_MAX_CONCURRENT_CHECKPOINTS);
        }
    };

    public BaseOperationRequestBuilder() {
        requestBody = new ExecuteStatementRequestBody();
    }

    public BaseOperationRequestBuilder(ExecuteStatementRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public ExecuteStatementRequestBody build() {
        Objects.requireNonNull(requestBody.getStatement(), "statement must have value");
        return requestBody;
    }

    @Override
    public BaseOperationRequestBuilder statement(String statement) {
        requestBody.statement(statement);
        return this;
    }

    public BaseOperationRequestBuilder statementSet(List<String> statements) {
        requestBody.statement(String.join(
                System.lineSeparator(),
                "EXECUTE STATEMENT SET",
                "BEGIN",
                String.join(";" + System.lineSeparator(), statements),
                "END"));
        return this;
    }

    public BaseOperationRequestBuilder statementSet(String... statements) {
        requestBody.statement(String.join(
                System.lineSeparator(),
                "EXECUTE STATEMENT SET",
                "BEGIN",
                String.join(";" + System.lineSeparator(), statements),
                "END"));
        return this;
    }

    @Override
    public BaseOperationRequestBuilder timeout(Long timeout) {
        requestBody.executionTimeout(timeout);
        return this;
    }

    @Override
    public CheckpointingBuilder streaming() {
        OperationRequestBuilder.super.streaming();
        return new CheckpointingBuilder(requestBody);
    }

    @Override
    public BaseOperationRequestBuilder batch() {
        OperationRequestBuilder.super.batch();
        requestBody.getExecutionConfig().keySet().removeIf(s -> keys.contains(s));
        return this;
    }

    @Override
    public BaseOperationRequestBuilder executeConfig(String key, String value) {
        requestBody.putExecutionConfigItem(key, value);
        return this;
    }

    public static class CheckpointingBuilder extends BaseOperationRequestBuilder {
        public CheckpointingBuilder(ExecuteStatementRequestBody requestBody) {
            super(requestBody);
        }

        public CheckpointingBuilder backend() {
            return backend("rocksdb");
        }

        public CheckpointingBuilder backend(String backend) {
            executeConfig(STATE_BACKEND, backend);
            return this;
        }

        public CheckpointingBuilder storage() {
            return storage("filesystem");
        }

        public CheckpointingBuilder storage(String storage) {
            executeConfig(STATE_CHECKPOINT_STORAGE, storage);
            return this;
        }

        public CheckpointingBuilder dir(String path) {
            executeConfig(STATE_CHECKPOINTS_DIR, path);
            return this;
        }

        public CheckpointingBuilder interval(Duration duration) {
            executeConfig(EXECUTION_CHECKPOINTING_INTERVAL, DurationFormatter.formatWithHighestUnit(duration));
            return this;
        }

        public CheckpointingBuilder minPause(Duration duration) {
            executeConfig(EXECUTION_CHECKPOINTING_MIN_PAUSE, DurationFormatter.formatWithHighestUnit(duration));
            return this;
        }

        public CheckpointingBuilder timeout(Duration duration) {
            executeConfig(EXECUTION_CHECKPOINTING_TIMEOUT, DurationFormatter.formatWithHighestUnit(duration));
            return this;
        }

        public CheckpointingBuilder maxConcurrentCheckpoints() {
            return maxConcurrentCheckpoints(1);
        }

        public CheckpointingBuilder maxConcurrentCheckpoints(int number) {
            executeConfig(EXECUTION_CHECKPOINTING_MAX_CONCURRENT_CHECKPOINTS, String.valueOf(number));
            return this;
        }

        public CheckpointingBuilder retainOnCancellation() {
            executeConfig(EXECUTION_CHECKPOINTING_EXTERNALIZED_CHECKPOINT_RETENTION, "RETAIN_ON_CANCELLATION");
            return this;
        }

        public CheckpointingBuilder deleteOnCancellation() {
            executeConfig(EXECUTION_CHECKPOINTING_EXTERNALIZED_CHECKPOINT_RETENTION, "DELETE_ON_CANCELLATION");
            return this;
        }

        public CheckpointingBuilder noExternalizedCheckpoints() {
            executeConfig(EXECUTION_CHECKPOINTING_EXTERNALIZED_CHECKPOINT_RETENTION, "NO_EXTERNALIZED_CHECKPOINTS");
            return this;
        }
    }
}
