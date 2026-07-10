
# AnnouncementDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`content` | string
`published` | boolean
`publishedAt` | string
`createdAt` | string

## Example

```typescript
import type { AnnouncementDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "content": null,
  "published": null,
  "publishedAt": null,
  "createdAt": null,
} satisfies AnnouncementDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AnnouncementDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


