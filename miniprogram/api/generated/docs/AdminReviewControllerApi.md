# AdminReviewControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**approve**](AdminReviewControllerApi.md#approve) | **POST** /api/v1/admin/reviews/{id}/approve |  |
| [**get**](AdminReviewControllerApi.md#get) | **GET** /api/v1/admin/reviews/{id} |  |
| [**list4**](AdminReviewControllerApi.md#list4) | **GET** /api/v1/admin/reviews |  |
| [**reject**](AdminReviewControllerApi.md#reject) | **POST** /api/v1/admin/reviews/{id}/reject |  |



## approve

> approve(id)



### Example

```ts
import {
  Configuration,
  AdminReviewControllerApi,
} from '';
import type { ApproveRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReviewControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies ApproveRequest;

  try {
    const data = await api.approve(body);
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

`void` (Empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## get

> ReviewDetailDto get(id)



### Example

```ts
import {
  Configuration,
  AdminReviewControllerApi,
} from '';
import type { GetRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReviewControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies GetRequest;

  try {
    const data = await api.get(body);
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

[**ReviewDetailDto**](ReviewDetailDto.md)

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


## list4

> ReviewListResponse list4(page, size)



### Example

```ts
import {
  Configuration,
  AdminReviewControllerApi,
} from '';
import type { List4Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReviewControllerApi();

  const body = {
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
  } satisfies List4Request;

  try {
    const data = await api.list4(body);
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
| **page** | `number` |  | [Optional] [Defaults to `1`] |
| **size** | `number` |  | [Optional] [Defaults to `20`] |

### Return type

[**ReviewListResponse**](ReviewListResponse.md)

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


## reject

> reject(id, reviewRejectRequest)



### Example

```ts
import {
  Configuration,
  AdminReviewControllerApi,
} from '';
import type { RejectRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReviewControllerApi();

  const body = {
    // number
    id: 789,
    // ReviewRejectRequest
    reviewRejectRequest: ...,
  } satisfies RejectRequest;

  try {
    const data = await api.reject(body);
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
| **reviewRejectRequest** | [ReviewRejectRequest](ReviewRejectRequest.md) |  | |

### Return type

`void` (Empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

