
# HotRankingItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`authorId` | number
`authorNickname` | string
`authorAvatarUrl` | string
`coverUrl` | string
`summary` | string
`viewCount` | number
`likeCount` | number
`commentCount` | number
`publishedAt` | string
`hotScore` | number
`rank` | number

## Example

```typescript
import type { HotRankingItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "authorId": null,
  "authorNickname": null,
  "authorAvatarUrl": null,
  "coverUrl": null,
  "summary": null,
  "viewCount": null,
  "likeCount": null,
  "commentCount": null,
  "publishedAt": null,
  "hotScore": null,
  "rank": null,
} satisfies HotRankingItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as HotRankingItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


