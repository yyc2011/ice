
# ReviewListItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`articleId` | number
`title` | string
`reviewType` | number
`priority` | number
`createdAt` | Date

## Example

```typescript
import type { ReviewListItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "articleId": null,
  "title": null,
  "reviewType": null,
  "priority": null,
  "createdAt": null,
} satisfies ReviewListItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as ReviewListItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


