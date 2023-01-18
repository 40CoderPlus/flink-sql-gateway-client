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

import com.fortycoderplus.flink.ext.sqlgateway.SessionRequestBuilder;
import com.fortycoderplus.flink.ext.sqlgateway.model.OpenSessionRequestBody;

public abstract class BaseSessionRequestBuilder<SELF extends BaseSessionRequestBuilder<SELF>>
        implements SessionRequestBuilder<SELF> {

    private final SELF self;
    protected final OpenSessionRequestBody requestBody = new OpenSessionRequestBody();

    @SuppressWarnings("unchecked")
    public BaseSessionRequestBuilder() {
        this.self = (SELF) this;
    }

    @Override
    public OpenSessionRequestBody build() {
        return requestBody;
    }

    @Override
    public SELF property(String key, String value) {
        requestBody.putPropertiesItem(key, value);
        return self;
    }

    @Override
    public SELF sessionName(String sessionName) {
        requestBody.sessionName(sessionName);
        return self;
    }
}
