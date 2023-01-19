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

import com.fortycoderplus.flink.ext.sqlgateway.model.OpenSessionRequestBody;
import java.util.stream.Stream;

public class YarnSessionRequestBuilder
        extends BaseSessionRequestBuilder<YarnSessionRequestBuilder, OpenSessionRequestBody> {

    public YarnSessionRequestBuilder() {
        super();
        this.property("execution.target", "yarn-session");
    }

    public YarnSessionRequestBuilder(OpenSessionRequestBody requestBody) {
        super(requestBody);
        this.property("execution.target", "yarn-session");
    }

    public YarnSessionRequestBuilder(String applicationId) {
        super();
        this.property("execution.target", "yarn-session");
        applicationId(applicationId);
    }

    public YarnSessionRequestBuilder applicationId(String applicationId) {
        this.property("yarn.application.id", applicationId);
        return this;
    }

    /**
     * High Availability YARN ResourceManager builder
     *
     * @return YarnHARequestBuilder
     */
    public YarnHARequestBuilder ha() {
        this.property("flink.yarn.resourcemanager.ha.enabled", Boolean.TRUE.toString());
        return new YarnHARequestBuilder(requestBody);
    }

    /**
     * Single YARN ResourceManager builder
     *
     * @return YarnRequestBuilder
     */
    public YarnRequestBuilder single() {
        return new YarnRequestBuilder(requestBody);
    }

    public static class YarnHARequestBuilder extends YarnSessionRequestBuilder {

        public YarnHARequestBuilder() {
            super();
        }

        public YarnHARequestBuilder(OpenSessionRequestBody requestBody) {
            super(requestBody);
        }

        public YarnHARequestBuilder rmIds(String rmIds) {
            this.property("flink.yarn.resourcemanager.ha.rm-ids", rmIds);
            return this;
        }

        public YarnHARequestBuilder clusterId(String clusterId) {
            this.property("flink.yarn.resourcemanager.cluster-id", clusterId);
            return this;
        }

        public YarnHARequestBuilder hostnameX(String rmId, String hostname) {
            checkRM(rmId);
            this.property("flink.yarn.resourcemanager.hostname." + rmId, hostname);
            return this;
        }

        public YarnHARequestBuilder webappAddressX(String rmId, String webappAddress) {
            checkRM(rmId);
            this.property("flink.yarn.resourcemanager.webapp.address." + rmId, webappAddress);
            return this;
        }

        public YarnHARequestBuilder failoverProxyProvider(String failoverProxyProvider) {
            this.property("flink.yarn.failover-proxy-provider", failoverProxyProvider);
            return this;
        }

        public YarnHARequestBuilder failoverProxyProvider() {
            this.property(
                    "flink.yarn.failover-proxy-provider",
                    "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider");
            return this;
        }

        private void checkRM(String rmId) {
            if (Stream.of(requestBody
                            .getProperties()
                            .getOrDefault("flink.yarn.resourcemanager.ha.rm-ids", "")
                            .split(","))
                    .noneMatch(rmId::equals)) {
                throw new IllegalArgumentException("Please set rmIds correct");
            }
        }
    }

    public static class YarnRequestBuilder extends YarnSessionRequestBuilder {

        public YarnRequestBuilder() {
            super();
        }

        public YarnRequestBuilder(OpenSessionRequestBody requestBody) {
            super(requestBody);
        }

        public YarnRequestBuilder address(String address) {
            this.property("flink.yarn.resourcemanager.address", address);
            return this;
        }

        public YarnRequestBuilder schedulerAddress(String schedulerAddress) {
            this.property("flink.yarn.resourcemanager.scheduler.address", schedulerAddress);
            return this;
        }

        public YarnRequestBuilder webappAddress(String webappAddress) {
            this.property("flink.yarn.resourcemanager.webapp.address", webappAddress);
            return this;
        }
    }
}
