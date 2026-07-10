import type { LoginResponse } from './generated/models/LoginResponse'
import { authApi } from './instances'
import { clearToken, getToken, setToken } from './token'

const DEV_OPENID_KEY = 'dev_openid'

export { clearToken, getToken, setToken }

export async function wechatLogin(code: string): Promise<LoginResponse> {
  const res = await authApi.wechatLogin({ wechatLoginRequest: { code } })
  if (res.token) {
    setToken(res.token)
  }
  return res
}

export async function devLogin(openid?: string): Promise<LoginResponse> {
  const res = await authApi.devLogin({ devLoginRequest: openid ? { openid } : {} })
  if (res.token) {
    setToken(res.token)
  }
  return res as LoginResponse
}

export async function ensureLogin(): Promise<string> {
  const existing = getToken()
  if (existing) {
    return existing
  }
  const storedOpenid = (wx.getStorageSync(DEV_OPENID_KEY) as string) || 'mp-p0-user'
  return new Promise((resolve, reject) => {
    wx.login({
      success: async () => {
        try {
          const code = `dev:${storedOpenid}`
          const res = await wechatLogin(code)
          resolve(res.token || '')
        } catch {
          try {
            const res = await devLogin(storedOpenid)
            resolve(res.token || '')
          } catch (e) {
            reject(e)
          }
        }
      },
      fail: (err) => reject(err),
    })
  })
}

export async function reloginOnUnauthorized(): Promise<void> {
  clearToken()
  await ensureLogin()
}
