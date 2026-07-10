import { Configuration } from './generated'

const TOKEN_KEY = 'admin_token'

export function getToken(): string {
  return localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export const apiConfig = new Configuration({
  basePath: '',
  middleware: [
    {
      pre: async (ctx) => {
        const token = getToken()
        if (token) {
          ctx.init.headers = {
            ...ctx.init.headers,
            Authorization: `Bearer ${token}`,
          }
        }
        return ctx
      },
    },
  ],
})
