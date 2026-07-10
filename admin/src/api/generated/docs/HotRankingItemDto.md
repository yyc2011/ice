
# HotRankingItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`authorNickname` | string
`viewCount` | number
`likeCount` | number
`hotScore` | number
`rank` | number

## Example

```typescript
import type { HotRankingItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "authorNickname": null,
  "viewCount": null,
  "likeCount": null,
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


