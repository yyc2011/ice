
# ReviewDetailDto


## Properties

Name | Type
------------ | -------------
`id` | number
`articleId` | number
`title` | string
`content` | string
`reviewType` | number
`appealText` | string
`aiScore` | number
`aiDimensions` | string
`createdAt` | Date

## Example

```typescript
import type { ReviewDetailDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "articleId": null,
  "title": null,
  "content": null,
  "reviewType": null,
  "appealText": null,
  "aiScore": null,
  "aiDimensions": null,
  "createdAt": null,
} satisfies ReviewDetailDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as ReviewDetailDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


