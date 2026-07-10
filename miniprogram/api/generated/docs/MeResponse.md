
# MeResponse


## Properties

Name | Type
------------ | -------------
`id` | number
`nickname` | string
`avatarUrl` | string
`level` | string
`levelName` | string
`bookCoinBalance` | number
`followerCount` | number
`followingCount` | number
`articleCount` | number
`role` | string
`wordLimit` | number

## Example

```typescript
import type { MeResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "nickname": null,
  "avatarUrl": null,
  "level": null,
  "levelName": null,
  "bookCoinBalance": null,
  "followerCount": null,
  "followingCount": null,
  "articleCount": null,
  "role": null,
  "wordLimit": null,
} satisfies MeResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as MeResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


