
# ReportDto


## Properties

Name | Type
------------ | -------------
`id` | number
`reporterId` | number
`targetType` | number
`targetId` | number
`reason` | string
`reasonDetail` | string
`status` | number
`createdAt` | string

## Example

```typescript
import type { ReportDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "reporterId": null,
  "targetType": null,
  "targetId": null,
  "reason": null,
  "reasonDetail": null,
  "status": null,
  "createdAt": null,
} satisfies ReportDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as ReportDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


