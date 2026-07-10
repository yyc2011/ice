
# CreateDraftRequest


## Properties

Name | Type
------------ | -------------
`title` | string
`content` | string
`tagIds` | Array&lt;number&gt;
`categoryId` | number
`topicId` | number
`coverUrl` | string
`imageUrls` | Array&lt;string&gt;

## Example

```typescript
import type { CreateDraftRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "title": null,
  "content": null,
  "tagIds": null,
  "categoryId": null,
  "topicId": null,
  "coverUrl": null,
  "imageUrls": null,
} satisfies CreateDraftRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CreateDraftRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


