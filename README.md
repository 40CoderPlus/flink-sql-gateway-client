# Notice

Some Changes in `flink_sql_gateway_rest_v1.yml`:

1. `session_handle` in path parameter change to type `UUID`;
2. `operation_handle` in path parameter change to type `UUID`;

# Build

Use [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) to generation client SDKs.

```shell
gradle clean
gradle openApiGenerate
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

To Be done

# Warning

See issue [FLINK-29881](https://issues.apache.org/jira/browse/FLINK-29881).

`FetchResultsResponseBody` incompatible with the true response of Flink SQL Gateway Fetch results of Operation API `/sessions/{session_handle}/operations/{operation_handle}/result/{token}`.
