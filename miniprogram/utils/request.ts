import { ResponseError } from '../api/generated/runtime'

export async function getApiErrorMessage(err: unknown, fallback = '操作失败'): Promise<string> {
  if (err instanceof ResponseError) {
    try {
      const body = (await err.response.json()) as { message?: string }
      if (body?.message) {
        return body.message
      }
    } catch {
      // ignore
    }
    return `请求失败 (${err.response.status})`
  }
  if (err instanceof Error) {
    return err.message
  }
  return fallback
}

export function showApiError(err: unknown, fallback = '操作失败'): void {
  void getApiErrorMessage(err, fallback).then((message) => {
    wx.showToast({ title: message.slice(0, 20), icon: 'none' })
  })
}

export const STATUS_LABELS: Record<string, string> = {
  draft: '草稿',
  reviewing: '审核中',
  published: '已发布',
  rejected: '已拒绝',
}

export const STATUS_COLORS: Record<string, string> = {
  draft: '#909399',
  reviewing: '#e6a23c',
  published: '#67c23a',
  rejected: '#f56c6c',
}
