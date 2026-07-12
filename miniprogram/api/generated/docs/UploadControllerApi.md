# UploadControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**serveFile**](UploadControllerApi.md#servefile) | **GET** /api/v1/uploads/files/** |  |
| [**uploadImage**](UploadControllerApi.md#uploadimage) | **POST** /api/v1/uploads/images |  |



## serveFile

> Blob serveFile()



### Example

```ts
import {
  Configuration,
  UploadControllerApi,
} from '';
import type { ServeFileRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new UploadControllerApi();

  try {
    const data = await api.serveFile();
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

**Blob**

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


## uploadImage

> UploadImageResponse uploadImage(file)



### Example

```ts
import {
  Configuration,
  UploadControllerApi,
} from '';
import type { UploadImageRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new UploadControllerApi();

  const body = {
    // Blob
    file: BINARY_DATA_HERE,
  } satisfies UploadImageRequest;

  try {
    const data = await api.uploadImage(body);
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
| **file** | `Blob` |  | [Defaults to `undefined`] |

### Return type

[**UploadImageResponse**](UploadImageResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `multipart/form-data`
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

