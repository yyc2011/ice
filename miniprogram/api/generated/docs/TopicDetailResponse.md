
# TopicDetailResponse


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
`likeCount` | number
`durationDays` | number
`rewardPoolAmount` | number
`startAt` | string
`endAt` | string
`creatorNickname` | string
`daysRemaining` | number

## Example

```typescript
import type { TopicDetailResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "title": null,
  "description": null,
  "coverUrl": null,
  "status": null,
  "articleCount": null,
  "viewCount": null,
  "likeCount": null,
  "durationDays": null,
  "rewardPoolAmount": null,
  "startAt": null,
  "endAt": null,
  "creatorNickname": null,
  "daysRemaining": null,
} satisfies TopicDetailResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as TopicDetailResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


