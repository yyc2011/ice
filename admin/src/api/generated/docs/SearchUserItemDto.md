
# SearchUserItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`nickname` | string
`avatarUrl` | string
`bio` | string
`articleCount` | number
`likeCount` | number

## Example

```typescript
import type { SearchUserItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "nickname": null,
  "avatarUrl": null,
  "bio": null,
  "articleCount": null,
  "likeCount": null,
} satisfies SearchUserItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as SearchUserItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


