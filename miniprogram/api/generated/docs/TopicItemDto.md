
# TopicItemDto


## Properties

Name | Type
------------ | -------------
`id` | number
`title` | string
`description` | string
`coverUrl` | string
`status` | string
`articleCount` | number
`viewCount` | number
`startAt` | string
`endAt` | string
`daysRemaining` | number

## Example

```typescript
import type { TopicItemDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "description": null,
  "coverUrl": null,
  "status": null,
  "articleCount": null,
  "viewCount": null,
  "startAt": null,
  "endAt": null,
  "daysRemaining": null,
} satisfies TopicItemDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as TopicItemDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


