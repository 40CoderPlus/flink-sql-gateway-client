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

package com.fortycoderplus.flink.sql.gateway.example;

import com.fortycoderplus.flink.sql.gateway.FlinkSqlGateway;
import com.fortycoderplus.flink.sql.gateway.api.DefaultApi;
import com.fortycoderplus.flink.sql.gateway.invoker.ApiException;
import com.fortycoderplus.flink.sql.gateway.model.ExecuteStatementRequestBody;
import com.fortycoderplus.flink.sql.gateway.model.ExecuteStatementResponseBody;
import com.fortycoderplus.flink.sql.gateway.model.OpenSessionRequestBody;
import com.fortycoderplus.flink.sql.gateway.model.OpenSessionResponseBody;
import java.util.UUID;

public class FlinkSqlGatewayExample {

    private FlinkSqlGatewayExample() {}

    public static void main(String[] args) throws ApiException {
        DefaultApi api = FlinkSqlGateway.sqlGatewayApi("http://127.0.0.1:8083");
        runOnYarn(api);
        runOnKubernetes(api);
    }

    private static void runOnKubernetes(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("jobmanager", "127.0.0.1:8081")
                .putPropertiesItem("kubernetes.cluster-id", "custom-flink-cluster")
                .putPropertiesItem("kubernetes.jobmanager.service-account", "flink")
                .putPropertiesItem("kubernetes.namespace", "flink-cluster")
                .putPropertiesItem("rest.address", "127.0.0.1")
                .putPropertiesItem("rest.port", "8081")
                .putPropertiesItem("execution.target", "kubernetes-session"));
        System.out.println(response.getSessionHandle());

        ExecuteStatementResponseBody statement1 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE datagen (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'datagen',\n"
                                + " 'rows-per-second'='10',\n"
                                + " 'fields.f_sequence.kind'='sequence',\n"
                                + " 'fields.f_sequence.start'='1',\n"
                                + " 'fields.f_sequence.end'='1000',\n"
                                + " 'fields.f_random.min'='1',\n"
                                + " 'fields.f_random.max'='1000',\n"
                                + " 'fields.f_random_str.length'='10'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement1.getOperationHandle());

        ExecuteStatementResponseBody statement2 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE blackhole_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'blackhole'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement2.getOperationHandle());

        ExecuteStatementResponseBody statement3 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("CREATE TABLE print_table  (\n" + " f_sequence INT,\n"
                                + " f_random INT,\n"
                                + " f_random_str STRING\n"
                                + ") WITH (\n"
                                + " 'connector' = 'print'\n"
                                + ")")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement3.getOperationHandle());

        ExecuteStatementResponseBody statement4 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("EXECUTE STATEMENT SET\n" + "BEGIN\n"
                                + "    insert into blackhole_table select * from datagen;\n"
                                + "    insert into print_table select * from datagen;\n"
                                + "END;")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on K8S Example"));

        System.out.println(statement4.getOperationHandle());
    }

    public static void runOnYarn(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "yarn-session")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.enabled", "true")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.rm-ids", "rm1,rm2")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm1", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm2", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.cluster-id", "yarn-cluster")
                .putPropertiesItem(
                        "flink.hadoop.yarn.client.failover-proxy-provider",
                        "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider")
                .putPropertiesItem("yarn.application.id", "application_1667789375191_XXXX"));
        System.out.println(response.getSessionHandle());
        ExecuteStatementResponseBody executeStatementResponseBody = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("select 1")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK on YARN Example"));
        System.out.println(executeStatementResponseBody.getOperationHandle());
    }

    public static void runOnYarnWithUDF(DefaultApi api) throws ApiException {
        OpenSessionResponseBody response = api.openSession(new OpenSessionRequestBody()
                .putPropertiesItem("execution.target", "yarn-session")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.enabled", "true")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.ha.rm-ids", "rm1,rm2")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm1", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.hostname.rm2", "yarn01")
                .putPropertiesItem("flink.hadoop.yarn.resourcemanager.cluster-id", "yarn-cluster")
                .putPropertiesItem(
                        "flink.hadoop.yarn.client.failover-proxy-provider",
                        "org.apache.hadoop.yarn.client.ConfiguredRMFailoverProxyProvider")
                .putPropertiesItem("yarn.application.id", "application_1667789375191_XXXX"));

        ExecuteStatementResponseBody statment1 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("create TEMPORARY FUNCTION \n"
                                + "    FakeFunction as 'com.fortycoderplus.flink.udf.FakeFunction'\n"
                                + "using JAR 'hdfs://MyHdfsService/udf-test/fake-func.jar'")
                        .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway UDF on YARN Example"));
        System.out.println(statment1.getOperationHandle());

        ExecuteStatementResponseBody statment2 = api.executeStatement(
                UUID.fromString(response.getSessionHandle()),
                new ExecuteStatementRequestBody()
                        .statement("select FakeFunction('Flink SQL Gateway UDF on YARN Example')")
                        .putExecutionConfigItem(
                                "pipeline.name", "Flink SQL Gateway UDF on YARN Example-" + UUID.randomUUID()));
        System.out.println(statment2.getOperationHandle());
    }
}
