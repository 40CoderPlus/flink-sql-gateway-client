# Notice

Default Open API specification version is v2. See in [Flink SQL Gateway REST API](https://nightlies.apache.org/flink/flink-docs-release-1.17/docs/dev/table/sql-gateway/rest/#rest-api)

Some Changes in [Open API v1 specification](spec/rest_v1_sql_gateway.yml),[Open API v2 specification](spec/rest_v2_sql_gateway.yml)

1. `session_handle` in path parameter change to type `UUID`;
2. `operation_handle` in path parameter change to type `UUID`;

# Build

Use [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) to generation client SDKs.

- Version V2

```shell
gradle clean
gradle openApiGenerate
gradle build -x test
```

- Version V1
```shell
gradle clean
gradle buildV1SDK
gradle build -x test
```

# Example

```java
DefaultApi api = new DefaultApi(new ApiClient());
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
            .putExecutionConfigItem("pipeline.name", "Flink SQL Gateway SDK Example"));
System.out.println(executeStatementResponseBody.getOperationHandle());
```

See more in `FlinkSqlGatewayExample`.

# Easy Builder

Build Flink SQl Gateway Rest API in fluent api.

## Build session request body

```java
SessionRequestBuilder.yarn()
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
```
See more example in [SessionRequestBuilderTest](easy-builder/src/test/java/com/fortycoderplus/flink/ext/sqlgateway/SessionRequestBuilderTest.java)

## Build execute statement request body

```java
OperationRequestBuilder.builder()
    .pipelineName("test")
    .streaming()
        .maxConcurrentCheckpoints(1)
        .storage()
        .backend()
        .noExternalizedCheckpoints()
    .statement("select * from kafka_table")
    .build();
```

See more example in [OperationRequestBuilderTest](easy-builder/src/test/java/com/fortycoderplus/flink/ext/sqlgateway/OperationRequestBuilderTest.java)

# Warning

See issue [FLINK-29881](https://issues.apache.org/jira/browse/FLINK-29881).

`FetchResultsResponseBody` incompatible with the true response of Flink SQL Gateway Fetch results of Operation API `/sessions/{session_handle}/operations/{operation_handle}/result/{token}`.
