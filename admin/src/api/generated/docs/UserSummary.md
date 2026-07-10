
# UserSummary


## Properties

Name | Type
------------ | -------------
`id` | number
`nickname` | string
`avatarUrl` | string
`level` | string
`levelName` | string
`bookCoinBalance` | number
`role` | string

## Example

```typescript
import type { UserSummary } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "nickname": null,
  "avatarUrl": null,
  "level": null,
  "levelName": null,
  "bookCoinBalance": null,
  "role": null,
} satisfies UserSummary

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as UserSummary
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


