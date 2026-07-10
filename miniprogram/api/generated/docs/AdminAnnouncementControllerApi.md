# AdminAnnouncementControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create3**](AdminAnnouncementControllerApi.md#create3) | **POST** /api/v1/admin/announcements |  |
| [**list3**](AdminAnnouncementControllerApi.md#list3) | **GET** /api/v1/admin/announcements |  |



## create3

> AnnouncementDto create3(createAnnouncementRequest)



### Example

```ts
import {
  Configuration,
  AdminAnnouncementControllerApi,
} from '';
import type { Create3Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminAnnouncementControllerApi();

  const body = {
    // CreateAnnouncementRequest
    createAnnouncementRequest: ...,
  } satisfies Create3Request;

  try {
    const data = await api.create3(body);
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
| **createAnnouncementRequest** | [CreateAnnouncementRequest](CreateAnnouncementRequest.md) |  | |

### Return type

[**AnnouncementDto**](AnnouncementDto.md)

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


## list3

> Array&lt;AnnouncementDto&gt; list3()



### Example

```ts
import {
  Configuration,
  AdminAnnouncementControllerApi,
} from '';
import type { List3Request } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AdminAnnouncementControllerApi();

  try {
    const data = await api.list3();
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

[**Array&lt;AnnouncementDto&gt;**](AnnouncementDto.md)

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

