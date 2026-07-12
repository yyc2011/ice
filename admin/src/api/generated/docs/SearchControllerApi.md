# SearchControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**search1**](SearchControllerApi.md#search1) | **GET** /api/v1/search |  |



## search1

> SearchResponse search1(q, type, page, size)



### Example

```ts
import {
  Configuration,
  SearchControllerApi,
} from '';
import type { Search1Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new SearchControllerApi();

  const body = {
    // string
    q: q_example,
    // string (optional)
    type: type_example,
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
  } satisfies Search1Request;

  try {
    const data = await api.search1(body);
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
| **q** | `string` |  | [Defaults to `undefined`] |
| **type** | `string` |  | [Optional] [Defaults to `&#39;title&#39;`] |
| **page** | `number` |  | [Optional] [Defaults to `1`] |
| **size** | `number` |  | [Optional] [Defaults to `20`] |

### Return type

[**SearchResponse**](SearchResponse.md)

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

