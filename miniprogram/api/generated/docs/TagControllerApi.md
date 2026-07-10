# TagControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create1**](TagControllerApi.md#create1) | **POST** /api/v1/tags |  |
| [**search**](TagControllerApi.md#search) | **GET** /api/v1/tags/search |  |



## create1

> TagItemDto create1(createTagRequest)



### Example

```ts
import {
  Configuration,
  TagControllerApi,
} from '';
import type { Create1Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TagControllerApi();

  const body = {
    // CreateTagRequest
    createTagRequest: ...,
  } satisfies Create1Request;

  try {
    const data = await api.create1(body);
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
| **createTagRequest** | [CreateTagRequest](CreateTagRequest.md) |  | |

### Return type

[**TagItemDto**](TagItemDto.md)

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


## search

> TagListResponse search(q)



### Example

```ts
import {
  Configuration,
  TagControllerApi,
} from '';
import type { SearchRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TagControllerApi();

  const body = {
    // string (optional)
    q: q_example,
  } satisfies SearchRequest;

  try {
    const data = await api.search(body);
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
| **q** | `string` |  | [Optional] [Defaults to `undefined`] |

### Return type

[**TagListResponse**](TagListResponse.md)

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

