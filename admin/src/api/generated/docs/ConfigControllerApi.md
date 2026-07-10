# ConfigControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**client**](ConfigControllerApi.md#client) | **GET** /api/v1/config/client |  |



## client

> ClientConfigResponse client()



### Example

```ts
import {
  Configuration,
  ConfigControllerApi,
} from '';
import type { ClientRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ConfigControllerApi();

  try {
    const data = await api.client();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**ClientConfigResponse**](ClientConfigResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

