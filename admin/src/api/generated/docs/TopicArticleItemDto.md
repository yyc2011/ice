
# TopicArticleItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`authorNickname` | string
`viewCount` | number
`likeCount` | number
`rank` | number
`publishedAt` | string
`hotScore` | number

## Example

```typescript
import type { TopicArticleItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "authorNickname": null,
  "viewCount": null,
  "likeCount": null,
  "rank": null,
  "publishedAt": null,
  "hotScore": null,
} satisfies TopicArticleItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as TopicArticleItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


