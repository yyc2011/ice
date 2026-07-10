/// <reference path="./types/index.d.ts" />

interface IAppOption {
  globalData: {
    userInfo?: WechatMiniprogram.UserInfo
    clientConfig?: {
      recharge_enabled: boolean
      recharge_custom_amount_enabled: boolean
    }
  }
  userInfoReadyCallback?: WechatMiniprogram.GetUserInfoSuccessCallback
  ensureLogin?: () => Promise<string>
}
