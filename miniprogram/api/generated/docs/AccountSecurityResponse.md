
# AccountSecurityResponse


## Properties

Name | Type
------------ | -------------
`phoneMasked` | string
`phoneVerified` | boolean
`passwordSet` | boolean
`wechatBound` | boolean
`accountName` | string
`accountNameSet` | boolean
`canChangeAccountName` | boolean
`accountNameNextChangeAt` | string

## Example

```typescript
import type { AccountSecurityResponse } from ''

// TODO: Update the object below with actual values
const example = {
  "phoneMasked": null,
  "phoneVerified": null,
  "passwordSet": null,
  "wechatBound": null,
  "accountName": null,
  "accountNameSet": null,
  "canChangeAccountName": null,
  "accountNameNextChangeAt": null,
} satisfies AccountSecurityResponse

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AccountSecurityResponse
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


