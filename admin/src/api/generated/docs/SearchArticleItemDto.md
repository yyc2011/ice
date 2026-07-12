
# SearchArticleItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`coverUrl` | string
`summary` | string
`authorId` | number
`authorNickname` | string
`authorAvatarUrl` | string
`authorLevelName` | string
`viewCount` | number
`publishedAt` | string

## Example

```typescript
import type { SearchArticleItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "coverUrl": null,
  "summary": null,
  "authorId": null,
  "authorNickname": null,
  "authorAvatarUrl": null,
  "authorLevelName": null,
  "viewCount": null,
  "publishedAt": null,
} satisfies SearchArticleItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as SearchArticleItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


