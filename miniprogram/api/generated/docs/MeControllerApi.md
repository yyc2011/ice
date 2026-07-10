# MeControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**accountSecurity**](MeControllerApi.md#accountsecurity) | **GET** /api/v1/me/account |  |
| [**bindPhone**](MeControllerApi.md#bindphoneoperation) | **POST** /api/v1/me/phone/bind |  |
| [**changeAccountName**](MeControllerApi.md#changeaccountnameoperation) | **POST** /api/v1/me/account-name |  |
| [**changePassword**](MeControllerApi.md#changepasswordoperation) | **POST** /api/v1/me/password |  |
| [**deactivate**](MeControllerApi.md#deactivate) | **DELETE** /api/v1/me |  |
| [**getMe**](MeControllerApi.md#getme) | **GET** /api/v1/me |  |
| [**listArticles**](MeControllerApi.md#listarticles) | **GET** /api/v1/me/articles |  |
| [**myTopics**](MeControllerApi.md#mytopics) | **GET** /api/v1/me/topics |  |
| [**updateWechatProfile**](MeControllerApi.md#updatewechatprofile) | **POST** /api/v1/me/wechat-profile |  |



## accountSecurity

> AccountSecurityResponse accountSecurity()



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { AccountSecurityRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  try {
    const data = await api.accountSecurity();
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

[**AccountSecurityResponse**](AccountSecurityResponse.md)

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


## bindPhone

> bindPhone(bindPhoneRequest)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { BindPhoneOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // BindPhoneRequest
    bindPhoneRequest: ...,
  } satisfies BindPhoneOperationRequest;

  try {
    const data = await api.bindPhone(body);
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
| **bindPhoneRequest** | [BindPhoneRequest](BindPhoneRequest.md) |  | |

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


## changeAccountName

> changeAccountName(changeAccountNameRequest)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { ChangeAccountNameOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // ChangeAccountNameRequest
    changeAccountNameRequest: ...,
  } satisfies ChangeAccountNameOperationRequest;

  try {
    const data = await api.changeAccountName(body);
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
| **changeAccountNameRequest** | [ChangeAccountNameRequest](ChangeAccountNameRequest.md) |  | |

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


## changePassword

> changePassword(changePasswordRequest)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { ChangePasswordOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // ChangePasswordRequest
    changePasswordRequest: ...,
  } satisfies ChangePasswordOperationRequest;

  try {
    const data = await api.changePassword(body);
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
| **changePasswordRequest** | [ChangePasswordRequest](ChangePasswordRequest.md) |  | |

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


## deactivate

> deactivate()



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { DeactivateRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  try {
    const data = await api.deactivate();
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


## getMe

> MeResponse getMe()



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { GetMeRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  try {
    const data = await api.getMe();
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

[**MeResponse**](MeResponse.md)

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


## listArticles

> MyArticlesResponse listArticles(status, page, size)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { ListArticlesRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // string (optional)
    status: status_example,
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
  } satisfies ListArticlesRequest;

  try {
    const data = await api.listArticles(body);
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
| **page** | `number` |  | [Optional] [Defaults to `1`] |
| **size** | `number` |  | [Optional] [Defaults to `20`] |

### Return type

[**MyArticlesResponse**](MyArticlesResponse.md)

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


## myTopics

> TopicListResponse myTopics(role)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { MyTopicsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // string (optional)
    role: role_example,
  } satisfies MyTopicsRequest;

  try {
    const data = await api.myTopics(body);
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
| **role** | `string` |  | [Optional] [Defaults to `&#39;created&#39;`] |

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


## updateWechatProfile

> UserSummary updateWechatProfile(wechatProfileRequest)



### Example

```ts
import {
  Configuration,
  MeControllerApi,
} from '';
import type { UpdateWechatProfileRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new MeControllerApi();

  const body = {
    // WechatProfileRequest
    wechatProfileRequest: ...,
  } satisfies UpdateWechatProfileRequest;

  try {
    const data = await api.updateWechatProfile(body);
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
| **wechatProfileRequest** | [WechatProfileRequest](WechatProfileRequest.md) |  | |

### Return type

[**UserSummary**](UserSummary.md)

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

