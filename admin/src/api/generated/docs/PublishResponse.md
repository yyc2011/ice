
# PublishResponse


## Properties

Name | Type
------------ | -------------
`id` | number
`status` | string
`publishedAt` | Date
`rejectReason` | string
`message` | string

## Example

```typescript
import type { PublishResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "status": null,
  "publishedAt": null,
  "rejectReason": null,
  "message": null,
} satisfies PublishResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as PublishResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


