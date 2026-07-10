import { defineStore } from 'pinia'
import { adminLogin, clearToken, devLogin, setToken } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    nickname: '',
    role: '',
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    async login(loginName: string, password: string) {
      const res = await adminLogin(loginName, password)
      this.token = res.token || ''
      this.nickname = res.user?.nickname || ''
      this.role = res.user?.role || ''
      setToken(this.token)
    },
    async devQuickLogin() {
      const res = await devLogin('admin')
      this.token = res.token || ''
      this.nickname = res.user?.nickname || ''
      this.role = res.user?.role || ''
      setToken(this.token)
    },
    logout() {
      this.token = ''
      this.nickname = ''
      this.role = ''
      clearToken()
    },
  },
})
