import { ensureLogin } from './api/auth'
import { configApi } from './api/instances'

App<IAppOption>({
  globalData: {
    clientConfig: undefined,
  },

  async onLaunch() {
    try {
      const config = await configApi.client()
      this.globalData.clientConfig = config
    } catch (e) {
      console.warn('加载客户端配置失败', e)
    }

    this.ensureLogin = async () => {
      try {
        return await ensureLogin()
      } catch (e) {
        console.error('登录失败', e)
        throw e
      }
    }

    try {
      await this.ensureLogin()
    } catch (e) {
      wx.showToast({ title: '登录失败', icon: 'none' })
    }
  },

  ensureLogin: undefined,
})
