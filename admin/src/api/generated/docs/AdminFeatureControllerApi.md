# AdminFeatureControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**list1**](AdminFeatureControllerApi.md#list1) | **GET** /api/v1/admin/features |  |
| [**update1**](AdminFeatureControllerApi.md#update1) | **POST** /api/v1/admin/features |  |



## list1

> Array&lt;FeatureConfigDto&gt; list1()



### Example

```ts
import {
  Configuration,
  AdminFeatureControllerApi,
} from '';
import type { List1Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminFeatureControllerApi();

  try {
    const data = await api.list1();
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

[**Array&lt;FeatureConfigDto&gt;**](FeatureConfigDto.md)

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


## update1

> update1(featureUpdateRequest)



### Example

```ts
import {
  Configuration,
  AdminFeatureControllerApi,
} from '';
import type { Update1Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminFeatureControllerApi();

  const body = {
    // FeatureUpdateRequest
    featureUpdateRequest: ...,
  } satisfies Update1Request;

  try {
    const data = await api.update1(body);
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
| **featureUpdateRequest** | [FeatureUpdateRequest](FeatureUpdateRequest.md) |  | |

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

