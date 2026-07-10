import type { LoginResponse } from './generated'
import { authApi } from './instances'
import { setToken } from './api-config'

export { clearToken, getToken, setToken } from './api-config'

export async function adminLogin(loginName: string, password: string): Promise<LoginResponse> {
  const res = await authApi.adminLogin({
    adminLoginRequest: { loginName, password },
  })
  if (res.token) {
    setToken(res.token)
  }
  return res
}

export async function devLogin(openid = 'admin'): Promise<LoginResponse> {
  const res = await authApi.devLogin({ devLoginRequest: { openid } })
  if (res.token) {
    setToken(res.token)
  }
  return res as LoginResponse
}
