/*
 * (c) Copyright 2022 40CoderPlus. All rights reserved.
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

package com.fortycoderplus.flink.sql.gateway;

import com.fortycoderplus.flink.sql.gateway.api.DefaultApi;
import com.fortycoderplus.flink.sql.gateway.invoker.ApiClient;
import com.fortycoderplus.flink.sql.gateway.invoker.ApiException;
import com.fortycoderplus.flink.sql.gateway.model.OpenSessionRequestBody;
import com.fortycoderplus.flink.sql.gateway.model.OpenSessionResponseBody;
import java.util.Map;

public class FlinkSqlGateway {

    private FlinkSqlGateway() {}

    public static DefaultApi sqlGatewayApi(String basePath) {
        ApiClient client = new ApiClient();
        client.updateBaseUri(basePath);
        return new DefaultApi(client);
    }

    public static DefaultApi sqlGatewayApi() {
        ApiClient client = new ApiClient();
        return new DefaultApi(client);
    }

    public static void main(String[] args) throws ApiException {
        DefaultApi api = new DefaultApi(new ApiClient());
        OpenSessionResponseBody openSessionResponseBody = api.openSession(
                new OpenSessionRequestBody().sessionName("example").properties(Map.of("foo", "bar")));
    }
}
