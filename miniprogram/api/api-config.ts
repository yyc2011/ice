import { Configuration } from './generated/runtime'
import { getToken } from './token'
import { wxFetch } from './wx-fetch'

export const API_BASE_URL = 'http://localhost:8080'

export const apiConfig = new Configuration({
  basePath: API_BASE_URL,
  fetchApi: wxFetch as any,
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
