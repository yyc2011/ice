# AuthControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**adminLogin**](AuthControllerApi.md#adminloginoperation) | **POST** /api/v1/auth/admin/login |  |
| [**devLogin**](AuthControllerApi.md#devloginoperation) | **POST** /api/v1/auth/dev/login |  |
| [**passwordLogin**](AuthControllerApi.md#passwordloginoperation) | **POST** /api/v1/auth/password/login |  |
| [**phoneLogin**](AuthControllerApi.md#phoneloginoperation) | **POST** /api/v1/auth/phone/login |  |
| [**sendSmsCode**](AuthControllerApi.md#sendsmscode) | **POST** /api/v1/auth/sms/code |  |
| [**wechatLogin**](AuthControllerApi.md#wechatloginoperation) | **POST** /api/v1/auth/wechat/login |  |



## adminLogin

> LoginResponse adminLogin(adminLoginRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { AdminLoginOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // AdminLoginRequest
    adminLoginRequest: ...,
  } satisfies AdminLoginOperationRequest;

  try {
    const data = await api.adminLogin(body);
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
| **adminLoginRequest** | [AdminLoginRequest](AdminLoginRequest.md) |  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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


## devLogin

> DevLoginResponse devLogin(devLoginRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { DevLoginOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // DevLoginRequest (optional)
    devLoginRequest: ...,
  } satisfies DevLoginOperationRequest;

  try {
    const data = await api.devLogin(body);
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
| **devLoginRequest** | [DevLoginRequest](DevLoginRequest.md) |  | [Optional] |

### Return type

[**DevLoginResponse**](DevLoginResponse.md)

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


## passwordLogin

> LoginResponse passwordLogin(passwordLoginRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { PasswordLoginOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // PasswordLoginRequest
    passwordLoginRequest: ...,
  } satisfies PasswordLoginOperationRequest;

  try {
    const data = await api.passwordLogin(body);
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
| **passwordLoginRequest** | [PasswordLoginRequest](PasswordLoginRequest.md) |  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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


## phoneLogin

> LoginResponse phoneLogin(phoneLoginRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { PhoneLoginOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // PhoneLoginRequest
    phoneLoginRequest: ...,
  } satisfies PhoneLoginOperationRequest;

  try {
    const data = await api.phoneLogin(body);
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
| **phoneLoginRequest** | [PhoneLoginRequest](PhoneLoginRequest.md) |  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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


## sendSmsCode

> SmsCodeResponse sendSmsCode(smsCodeRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { SendSmsCodeRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // SmsCodeRequest
    smsCodeRequest: ...,
  } satisfies SendSmsCodeRequest;

  try {
    const data = await api.sendSmsCode(body);
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
| **smsCodeRequest** | [SmsCodeRequest](SmsCodeRequest.md) |  | |

### Return type

[**SmsCodeResponse**](SmsCodeResponse.md)

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


## wechatLogin

> LoginResponse wechatLogin(wechatLoginRequest)



### Example

```ts
import {
  Configuration,
  AuthControllerApi,
} from '';
import type { WechatLoginOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AuthControllerApi();

  const body = {
    // WechatLoginRequest
    wechatLoginRequest: ...,
  } satisfies WechatLoginOperationRequest;

  try {
    const data = await api.wechatLogin(body);
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
| **wechatLoginRequest** | [WechatLoginRequest](WechatLoginRequest.md) |  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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

