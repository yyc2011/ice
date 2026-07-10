# RankingControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**hot**](RankingControllerApi.md#hot) | **GET** /api/v1/rankings/hot |  |



## hot

> HotRankingResponse hot(window, size)



### Example

```ts
import {
  Configuration,
  RankingControllerApi,
} from '';
import type { HotRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new RankingControllerApi();

  const body = {
    // string (optional)
    window: window_example,
    // number (optional)
    size: 56,
  } satisfies HotRequest;

  try {
    const data = await api.hot(body);
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
| **window** | `string` |  | [Optional] [Defaults to `&#39;24h&#39;`] |
| **size** | `number` |  | [Optional] [Defaults to `10`] |

### Return type

[**HotRankingResponse**](HotRankingResponse.md)

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

