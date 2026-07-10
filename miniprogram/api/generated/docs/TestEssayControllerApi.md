# TestEssayControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getList**](TestEssayControllerApi.md#getlist) | **POST** /api/test/essay-list |  |



## getList

> Array&lt;TestEssayDto&gt; getList(testRequest)



### Example

```ts
import {
  Configuration,
  TestEssayControllerApi,
} from '';
import type { GetListRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TestEssayControllerApi();

  const body = {
    // TestRequest
    testRequest: ...,
  } satisfies GetListRequest;

  try {
    const data = await api.getList(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **testRequest** | [TestRequest](TestRequest.md) |  | |

### Return type

[**Array&lt;TestEssayDto&gt;**](TestEssayDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

