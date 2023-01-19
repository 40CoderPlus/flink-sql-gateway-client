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

import static org.junit.jupiter.api.Assertions.*;

import com.fortycoderplus.flink.ext.sqlgateway.model.OpenSessionRequestBody;
import org.junit.jupiter.api.Test;

class SessionRequestBuilderTest {

    @Test
    void sessionName() {
        assertEquals(
                "test", SessionRequestBuilder.yarn().sessionName("test").build().getSessionName());
    }

    @Test
    void savepoint() {
        assertEquals(
                "/test",
                SessionRequestBuilder.yarn()
                        .savepoint("/test")
                        .build()
                        .getProperties()
                        .get("execution.savepoint.path"));
    }

    @org.junit.jupiter.api.Test
    void yarn() {
        OpenSessionRequestBody haBody = SessionRequestBuilder.yarn()
                .sessionName("ha yarn session")
                .applicationId("application_1667789375191_XXXX")
                .savepoint("hdfs://fortycoderplus/flink/savepoints/savepoint-c12453-134defccc7c1")
                .ha()
                .clusterId("fortycoderplus")
                .rmIds("yarn1,yarn2")
                .hostnameX("yarn1", "yarn1.fortycoderplus")
                .hostnameX("yarn2", "yarn2.fortycoderplus")
                .failoverProxyProvider()
                .webappAddressX("yarn1", "http://yarn1.fortycoderplus:8080")
                .webappAddressX("yarn2", "http://yarn2.fortycoderplus:8080")
                .build();

        assertEquals(11, haBody.getProperties().size());

        OpenSessionRequestBody singleBody = SessionRequestBuilder.yarn()
                .sessionName("ha yarn session")
                .applicationId("application_1667789375191_XXXX")
                .savepoint("hdfs://fortycoderplus/fortycoderplus/flink/savepoints/savepoint-c12453-134defccc7c1")
                .single()
                .address("yarn.fortycoderplus")
                .schedulerAddress("yarn.fortycoderplus")
                .webappAddress("http://yarn.fortycoderplus:8080")
                .build();

        assertEquals(6, singleBody.getProperties().size());
    }

    @org.junit.jupiter.api.Test
    void kubernetes() {
        OpenSessionRequestBody body = SessionRequestBuilder.kubernetes()
                .jobmanager("flink-session.flink-cluster.svc.cluster.local:8081")
                .serviceAccount("fortycoderplus")
                .clusterId("flink.fortycoderplus")
                .namespace("flink-cluster")
                .restAddress("flink-session.flink-cluster.svc.cluster.local")
                .restPort(8081)
                .build();

        assertEquals(7, body.getProperties().size());
    }
}
