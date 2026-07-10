import { ResponseError } from './generated'

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
