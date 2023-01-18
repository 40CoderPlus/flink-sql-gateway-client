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

public class kubernetesSessionRequestBuilder extends BaseSessionRequestBuilder<kubernetesSessionRequestBuilder> {
    public kubernetesSessionRequestBuilder() {
        super();
        this.property("execution.target", "kubernetes-session");
    }

    public kubernetesSessionRequestBuilder jobmanager(String jobmanager) {
        property("jobmanager", jobmanager);
        return this;
    }

    public kubernetesSessionRequestBuilder clusterId(String clusterId) {
        property("kubernetes.cluster-id", clusterId);
        return this;
    }

    public kubernetesSessionRequestBuilder namespace(String namespace) {
        property("kubernetes.namespace", namespace);
        return this;
    }

    public kubernetesSessionRequestBuilder serviceAccount(String serviceAccount) {
        property("kubernetes.jobmanager.service-account", serviceAccount);
        return this;
    }

    public kubernetesSessionRequestBuilder restAddress(String restAddress) {
        property("restAddress", restAddress);
        return this;
    }

    public kubernetesSessionRequestBuilder restPort(int restPort) {
        property("restPort", String.valueOf(restPort));
        return this;
    }
}
