# NotificationControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listNotifications**](NotificationControllerApi.md#listnotifications) | **GET** /api/v1/messages/notifications |  |
| [**markRead**](NotificationControllerApi.md#markread) | **POST** /api/v1/messages/notifications/read |  |



## listNotifications

> NotificationListResponse listNotifications(page, size)



### Example

```ts
import {
  Configuration,
  NotificationControllerApi,
} from '';
import type { ListNotificationsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new NotificationControllerApi();

  const body = {
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
  } satisfies ListNotificationsRequest;

  try {
    const data = await api.listNotifications(body);
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

[**NotificationListResponse**](NotificationListResponse.md)

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


## markRead

> markRead(markNotificationsReadRequest)



### Example

```ts
import {
  Configuration,
  NotificationControllerApi,
} from '';
import type { MarkReadRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new NotificationControllerApi();

  const body = {
    // MarkNotificationsReadRequest (optional)
    markNotificationsReadRequest: ...,
  } satisfies MarkReadRequest;

  try {
    const data = await api.markRead(body);
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
| **markNotificationsReadRequest** | [MarkNotificationsReadRequest](MarkNotificationsReadRequest.md) |  | [Optional] |

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

