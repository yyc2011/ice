import { clearToken } from './token'

// wx.request 适配器，供 openapi-generator 生成的客户端使用
export const wxFetch = (url: string, init?: RequestInit): Promise<any> => {
  return new Promise((resolve, reject) => {
    wx.request({
      url,
      method: (init?.method as any) || 'GET',
      header: (init?.headers as any) || {},
      data: init?.body as any,
      success: (res: any) => {
        if (res.statusCode === 401) {
          clearToken()
        }
        const mockResponse = {
          status: res.statusCode,
          ok: res.statusCode >= 200 && res.statusCode < 300,
          headers: { get: (key: string) => res.header?.[key] ?? null },
          json: () => Promise.resolve(res.data),
          text: () => Promise.resolve(JSON.stringify(res.data)),
          clone: function () {
            return this
          },
        }
        resolve(mockResponse)
      },
      fail: (err: any) => reject(err),
    })
  })
}
