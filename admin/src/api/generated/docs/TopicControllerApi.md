# TopicControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](TopicControllerApi.md#create) | **POST** /api/v1/topics |  |
| [**detail**](TopicControllerApi.md#detail) | **GET** /api/v1/topics/{id} |  |
| [**list**](TopicControllerApi.md#list) | **GET** /api/v1/topics |  |
| [**ongoing**](TopicControllerApi.md#ongoing) | **GET** /api/v1/topics/ongoing |  |
| [**selectable**](TopicControllerApi.md#selectable) | **GET** /api/v1/topics/selectable |  |



## create

> CreateTopicResponse create(createTopicRequest)



### Example

```ts
import {
  Configuration,
  TopicControllerApi,
} from '';
import type { CreateRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TopicControllerApi();

  const body = {
    // CreateTopicRequest
    createTopicRequest: ...,
  } satisfies CreateRequest;

  try {
    const data = await api.create(body);
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
| **createTopicRequest** | [CreateTopicRequest](CreateTopicRequest.md) |  | |

### Return type

[**CreateTopicResponse**](CreateTopicResponse.md)

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


## detail

> TopicDetailResponse detail(id)



### Example

```ts
import {
  Configuration,
  TopicControllerApi,
} from '';
import type { DetailRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TopicControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies DetailRequest;

  try {
    const data = await api.detail(body);
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
| **id** | `number` |  | [Defaults to `undefined`] |

### Return type

[**TopicDetailResponse**](TopicDetailResponse.md)

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


## list

> TopicListResponse list(status, size, period, mode)



### Example

```ts
import {
  Configuration,
  TopicControllerApi,
} from '';
import type { ListRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TopicControllerApi();

  const body = {
    // string (optional)
    status: status_example,
    // number (optional)
    size: 56,
    // string (optional)
    period: period_example,
    // string (optional)
    mode: mode_example,
  } satisfies ListRequest;

  try {
    const data = await api.list(body);
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
| **status** | `string` |  | [Optional] [Defaults to `undefined`] |
| **size** | `number` |  | [Optional] [Defaults to `10`] |
| **period** | `string` |  | [Optional] [Defaults to `undefined`] |
| **mode** | `string` |  | [Optional] [Defaults to `undefined`] |

### Return type

[**TopicListResponse**](TopicListResponse.md)

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


## ongoing

> TopicListResponse ongoing(size)



### Example

```ts
import {
  Configuration,
  TopicControllerApi,
} from '';
import type { OngoingRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TopicControllerApi();

  const body = {
    // number (optional)
    size: 56,
  } satisfies OngoingRequest;

  try {
    const data = await api.ongoing(body);
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
| **size** | `number` |  | [Optional] [Defaults to `5`] |

### Return type

[**TopicListResponse**](TopicListResponse.md)

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


## selectable

> TopicListResponse selectable()



### Example

```ts
import {
  Configuration,
  TopicControllerApi,
} from '';
import type { SelectableRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new TopicControllerApi();

  try {
    const data = await api.selectable();
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

[**TopicListResponse**](TopicListResponse.md)

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

