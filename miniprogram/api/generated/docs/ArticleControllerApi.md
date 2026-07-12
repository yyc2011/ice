# ArticleControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createDraft**](ArticleControllerApi.md#createdraftoperation) | **POST** /api/v1/articles/drafts |  |
| [**featured**](ArticleControllerApi.md#featured) | **GET** /api/v1/articles/featured |  |
| [**getArticle**](ArticleControllerApi.md#getarticle) | **GET** /api/v1/articles/{id} |  |
| [**publish**](ArticleControllerApi.md#publish) | **POST** /api/v1/articles/{id}/publish |  |
| [**reviewAppeal**](ArticleControllerApi.md#reviewappealoperation) | **POST** /api/v1/articles/{id}/review-appeal |  |
| [**updateDraft**](ArticleControllerApi.md#updatedraft) | **PUT** /api/v1/articles/{id} |  |



## createDraft

> CreateDraftResponse createDraft(createDraftRequest)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { CreateDraftOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // CreateDraftRequest
    createDraftRequest: ...,
  } satisfies CreateDraftOperationRequest;

  try {
    const data = await api.createDraft(body);
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
| **createDraftRequest** | [CreateDraftRequest](CreateDraftRequest.md) |  | |

### Return type

[**CreateDraftResponse**](CreateDraftResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## featured

> FeaturedArticlesResponse featured(page, size)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { FeaturedRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
  } satisfies FeaturedRequest;

  try {
    const data = await api.featured(body);
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
| **size** | `number` |  | [Optional] [Defaults to `10`] |

### Return type

[**FeaturedArticlesResponse**](FeaturedArticlesResponse.md)

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


## getArticle

> ArticleDetailResponse getArticle(id)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { GetArticleRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies GetArticleRequest;

  try {
    const data = await api.getArticle(body);
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

[**ArticleDetailResponse**](ArticleDetailResponse.md)

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


## publish

> PublishResponse publish(id)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { PublishRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // number
    id: 789,
  } satisfies PublishRequest;

  try {
    const data = await api.publish(body);
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

[**PublishResponse**](PublishResponse.md)

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


## reviewAppeal

> ReviewAppealResponse reviewAppeal(id, reviewAppealRequest)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { ReviewAppealOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // number
    id: 789,
    // ReviewAppealRequest
    reviewAppealRequest: ...,
  } satisfies ReviewAppealOperationRequest;

  try {
    const data = await api.reviewAppeal(body);
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
| **reviewAppealRequest** | [ReviewAppealRequest](ReviewAppealRequest.md) |  | |

### Return type

[**ReviewAppealResponse**](ReviewAppealResponse.md)

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


## updateDraft

> CreateDraftResponse updateDraft(id, createDraftRequest)



### Example

```ts
import {
  Configuration,
  ArticleControllerApi,
} from '';
import type { UpdateDraftRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ArticleControllerApi();

  const body = {
    // number
    id: 789,
    // CreateDraftRequest
    createDraftRequest: ...,
  } satisfies UpdateDraftRequest;

  try {
    const data = await api.updateDraft(body);
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
| **createDraftRequest** | [CreateDraftRequest](CreateDraftRequest.md) |  | |

### Return type

[**CreateDraftResponse**](CreateDraftResponse.md)

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

