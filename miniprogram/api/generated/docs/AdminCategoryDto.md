
# AdminCategoryDto


## Properties

Name | Type
------------ | -------------
`id` | number
`parentId` | number
`name` | string
`isHomeRecommended` | boolean
`homeSortOrder` | number

## Example

```typescript
import type { AdminCategoryDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "parentId": null,
  "name": null,
  "isHomeRecommended": null,
  "homeSortOrder": null,
} satisfies AdminCategoryDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AdminCategoryDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


