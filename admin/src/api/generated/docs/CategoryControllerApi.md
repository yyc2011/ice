# CategoryControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**preview**](CategoryControllerApi.md#preview) | **GET** /api/v1/categories/preview |  |
| [**tree**](CategoryControllerApi.md#tree) | **GET** /api/v1/categories/tree |  |



## preview

> CategoryPreviewResponse preview()



### Example

```ts
import {
  Configuration,
  CategoryControllerApi,
} from '';
import type { PreviewRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new CategoryControllerApi();

  try {
    const data = await api.preview();
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

[**CategoryPreviewResponse**](CategoryPreviewResponse.md)

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


## tree

> CategoryTreeResponse tree()



### Example

```ts
import {
  Configuration,
  CategoryControllerApi,
} from '';
import type { TreeRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new CategoryControllerApi();

  try {
    const data = await api.tree();
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

[**CategoryTreeResponse**](CategoryTreeResponse.md)

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

