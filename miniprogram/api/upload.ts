import { API_BASE_URL } from './api-config'
import { getToken } from './token'

export interface UploadImageResult {
  url: string
}

/** 小程序 multipart 上传（OpenAPI FormData 在小程序不可用） */
export function uploadImage(filePath: string): Promise<UploadImageResult> {
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${API_BASE_URL}/api/v1/uploads/images`,
      filePath,
      name: 'file',
      header: {
        Authorization: `Bearer ${getToken()}`,
      },
      success: (res) => {
        if (res.statusCode < 200 || res.statusCode >= 300) {
          reject(new Error(`上传失败(${res.statusCode})`))
          return
        }
        try {
          const data = JSON.parse(res.data as string) as UploadImageResult
          if (!data.url) {
            reject(new Error('上传响应缺少 url'))
            return
          }
          resolve(data)
        } catch (e) {
          reject(e)
        }
      },
      fail: reject,
    })
  })
}
