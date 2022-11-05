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
OpenSessionResponseBody openSessionResponseBody = api.openSession(
        new OpenSessionRequestBody().sessionName("example").properties(Map.of("foo", "bar")));
```

# Warning

See issue [FLINK-29881](https://issues.apache.org/jira/browse/FLINK-29881).

`FetchResultsResponseBody` incompatible with the true response of Flink SQL Gateway Fetch results of Operation API `/sessions/{session_handle}/operations/{operation_handle}/result/{token}`.
