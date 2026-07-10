
# ReviewListResponse


## Properties

Name | Type
------------ | -------------
`items` | [Array&lt;ReviewListItemDto&gt;](ReviewListItemDto.md)
`total` | number
`page` | number
`size` | number

## Example

```typescript
import type { ReviewListResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "items": null,
  "total": null,
  "page": null,
  "size": null,
} satisfies ReviewListResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as ReviewListResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


