# AdminReportControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**list5**](AdminReportControllerApi.md#list5) | **GET** /api/v1/admin/reports |  |
| [**resolve**](AdminReportControllerApi.md#resolve) | **POST** /api/v1/admin/reports/{id}/resolve |  |



## list5

> Array&lt;ReportDto&gt; list5()



### Example

```ts
import {
  Configuration,
  AdminReportControllerApi,
} from '';
import type { List5Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReportControllerApi();

  try {
    const data = await api.list5();
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

[**Array&lt;ReportDto&gt;**](ReportDto.md)

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


## resolve

> resolve(id, reportResolveRequest)



### Example

```ts
import {
  Configuration,
  AdminReportControllerApi,
} from '';
import type { ResolveRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminReportControllerApi();

  const body = {
    // number
    id: 789,
    // ReportResolveRequest
    reportResolveRequest: ...,
  } satisfies ResolveRequest;

  try {
    const data = await api.resolve(body);
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
| **reportResolveRequest** | [ReportResolveRequest](ReportResolveRequest.md) |  | |

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

