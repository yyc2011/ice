# AdminCategoryControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**_delete**](AdminCategoryControllerApi.md#_delete) | **DELETE** /api/v1/admin/categories/{id} |  |
| [**create2**](AdminCategoryControllerApi.md#create2) | **POST** /api/v1/admin/categories |  |
| [**list2**](AdminCategoryControllerApi.md#list2) | **GET** /api/v1/admin/categories |  |
| [**update**](AdminCategoryControllerApi.md#update) | **PUT** /api/v1/admin/categories/{id} |  |



## _delete

> _delete(id)



### Example

```ts
import {
  Configuration,
  AdminCategoryControllerApi,
} from '';
import type { DeleteRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminCategoryControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies DeleteRequest;

  try {
    const data = await api._delete(body);
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


## create2

> AdminCategoryDto create2(adminCategoryRequest)



### Example

```ts
import {
  Configuration,
  AdminCategoryControllerApi,
} from '';
import type { Create2Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminCategoryControllerApi();

  const body = {
    // AdminCategoryRequest
    adminCategoryRequest: ...,
  } satisfies Create2Request;

  try {
    const data = await api.create2(body);
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
| **adminCategoryRequest** | [AdminCategoryRequest](AdminCategoryRequest.md) |  | |

### Return type

[**AdminCategoryDto**](AdminCategoryDto.md)

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


## list2

> Array&lt;AdminCategoryDto&gt; list2()



### Example

```ts
import {
  Configuration,
  AdminCategoryControllerApi,
} from '';
import type { List2Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminCategoryControllerApi();

  try {
    const data = await api.list2();
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

[**Array&lt;AdminCategoryDto&gt;**](AdminCategoryDto.md)

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


## update

> AdminCategoryDto update(id, adminCategoryRequest)



### Example

```ts
import {
  Configuration,
  AdminCategoryControllerApi,
} from '';
import type { UpdateRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminCategoryControllerApi();

  const body = {
    // number
    id: 789,
    // AdminCategoryRequest
    adminCategoryRequest: ...,
  } satisfies UpdateRequest;

  try {
    const data = await api.update(body);
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
| **adminCategoryRequest** | [AdminCategoryRequest](AdminCategoryRequest.md) |  | |

### Return type

[**AdminCategoryDto**](AdminCategoryDto.md)

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

