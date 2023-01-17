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

import com.fortycoderplus.flink.ext.sqlgateway.impl.YarnSessionRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.impl.kubernetesSessionRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.model.OpenSessionRequestBody;
import java.util.Map;

public interface SessionRequestBuilder<SELF> {

    static YarnSessionRequestBuilder yarn() {
        return new YarnSessionRequestBuilder();
    }

    static kubernetesSessionRequestBuilder kubernetes() {
        return new kubernetesSessionRequestBuilder();
    }

    /**
     * Build OpenSessionRequestBody
     *
     * @return OpenSessionRequestBody
     */
    OpenSessionRequestBody build();

    /**
     * Add a property
     *
     * @param key property key
     * @param value property key
     * @return SELF return builder
     */
    SELF property(String key, String value);

    /**
     * Add properties
     *
     * @param properties properties
     * @return SELF return builder
     */
    @SuppressWarnings("unchecked")
    default SELF properties(Map<String, String> properties) {
        properties.forEach(this::property);
        return (SELF) this;
    }
}
