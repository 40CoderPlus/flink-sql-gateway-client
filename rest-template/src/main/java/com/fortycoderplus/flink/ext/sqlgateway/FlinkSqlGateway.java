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

import com.fortycoderplus.flink.ext.sqlgateway.api.DefaultApi;
import com.fortycoderplus.flink.ext.sqlgateway.invoker.ApiClient;

public class FlinkSqlGateway {

    private FlinkSqlGateway() {}

    public static DefaultApi sqlGatewayApi(String basePath) {
        return new DefaultApi(new ApiClient().setBasePath(basePath));
    }

    public static DefaultApi sqlGatewayApi() {
        return new DefaultApi(new ApiClient());
    }
}
