
# SearchResponse


## Properties

Name | Type
------------ | -------------
`type` | string
`q` | string
`total` | number
`page` | number
`size` | number
`articles` | [Array&lt;SearchArticleItemDto&gt;](SearchArticleItemDto.md)
`users` | [Array&lt;SearchUserItemDto&gt;](SearchUserItemDto.md)
`topics` | [Array&lt;TopicItemDto&gt;](TopicItemDto.md)

## Example

```typescript
import type { SearchResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "type": null,
  "q": null,
  "total": null,
  "page": null,
  "size": null,
  "articles": null,
  "users": null,
  "topics": null,
} satisfies SearchResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as SearchResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


