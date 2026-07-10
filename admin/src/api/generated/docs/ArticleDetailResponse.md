
# ArticleDetailResponse


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`content` | string
`status` | string
`rejectReason` | string
`aiQualityScore` | number
`tags` | [Array&lt;TagDto&gt;](TagDto.md)
`latestReview` | [LatestReviewDto](LatestReviewDto.md)
`publishedAt` | Date
`createdAt` | Date

## Example

```typescript
import type { ArticleDetailResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "content": null,
  "status": null,
  "rejectReason": null,
  "aiQualityScore": null,
  "tags": null,
  "latestReview": null,
  "publishedAt": null,
  "createdAt": null,
} satisfies ArticleDetailResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as ArticleDetailResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


