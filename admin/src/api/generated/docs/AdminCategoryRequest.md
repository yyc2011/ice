
# AdminCategoryRequest


## Properties

Name | Type
------------ | -------------
`parentId` | number
`name` | string
`sortOrder` | number
`isHomeRecommended` | boolean
`homeSortOrder` | number

## Example

```typescript
import type { AdminCategoryRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "parentId": null,
  "name": null,
  "sortOrder": null,
  "isHomeRecommended": null,
  "homeSortOrder": null,
} satisfies AdminCategoryRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AdminCategoryRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


