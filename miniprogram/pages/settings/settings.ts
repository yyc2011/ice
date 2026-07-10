import { clearToken } from '../../api/auth'
import { authApi, meApi } from '../../api/instances'
import type { AccountSecurityResponse } from '../../api/generated/models/AccountSecurityResponse'
import { showApiError } from '../../utils/request'

Page({
  data: {
    account: null as AccountSecurityResponse | null,
    showPhoneDialog: false,
    showPasswordDialog: false,
    showAccountDialog: false,
    phoneInput: '',
    codeInput: '',
    accountInput: '',
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
    accountNameInput: '',
    accountPasswordInput: '',
    countdown: 0,
    deactivateConfirm: '',
    showDeactivate: false,
  },

  onShow() {
    this.loadAccount()
  },

  async loadAccount() {
    try {
      const account = await meApi.accountSecurity()
      this.setData({ account })
    } catch (e) {
      showApiError(e)
    }
  },

  openPhoneDialog() {
    this.setData({ showPhoneDialog: true, phoneInput: '', codeInput: '' })
  },

  closePhoneDialog() {
    this.setData({ showPhoneDialog: false })
  },

  openPasswordDialog() {
    this.setData({
      showPasswordDialog: true,
      accountInput: '',
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
    })
  },

  closePasswordDialog() {
    this.setData({ showPasswordDialog: false })
  },

  openAccountDialog() {
    const account = this.data.account
    if (!account?.accountNameSet) {
      wx.showToast({ title: '请先设置密码并设置账号', icon: 'none' })
      return
    }
    if (!account.canChangeAccountName) {
      wx.showToast({
        title: account.accountNameNextChangeAt
          ? `下次可修改：${account.accountNameNextChangeAt}`
          : '90 天内仅可修改 1 次',
        icon: 'none',
      })
      return
    }
    this.setData({
      showAccountDialog: true,
      accountNameInput: '',
      accountPasswordInput: '',
    })
  },

  closeAccountDialog() {
    this.setData({ showAccountDialog: false })
  },

  onPhoneInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ phoneInput: e.detail as string })
  },

  onCodeInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ codeInput: e.detail as string })
  },

  onAccountInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ accountInput: e.detail as string })
  },

  onOldPasswordInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ oldPassword: e.detail as string })
  },

  onNewPasswordInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ newPassword: e.detail as string })
  },

  onConfirmPasswordInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ confirmPassword: e.detail as string })
  },

  onAccountNameInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ accountNameInput: e.detail as string })
  },

  onAccountPasswordInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ accountPasswordInput: e.detail as string })
  },

  async sendCode() {
    if (!/^1\d{10}$/.test(this.data.phoneInput)) {
      wx.showToast({ title: '请输入正确手机号', icon: 'none' })
      return
    }
    try {
      await authApi.sendSmsCode({
        smsCodeRequest: { phone: this.data.phoneInput, purpose: 'bind' },
      })
      wx.showToast({ title: '验证码已发送', icon: 'success' })
      this.setData({ countdown: 60 })
      const timer = setInterval(() => {
        const next = this.data.countdown - 1
        if (next <= 0) {
          clearInterval(timer)
          this.setData({ countdown: 0 })
        } else {
          this.setData({ countdown: next })
        }
      }, 1000)
    } catch (e) {
      showApiError(e)
    }
  },

  async submitBindPhone() {
    try {
      await meApi.bindPhone({
        bindPhoneRequest: {
          phone: this.data.phoneInput,
          code: this.data.codeInput,
        },
      })
      wx.showToast({ title: '绑定成功', icon: 'success' })
      this.closePhoneDialog()
      this.loadAccount()
    } catch (e) {
      showApiError(e)
    }
  },

  async submitPassword() {
    const { account, newPassword, confirmPassword, accountInput, oldPassword } = this.data
    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' })
      return
    }
    if (!account?.accountNameSet) {
      if (!/^[a-zA-Z0-9_]{1,15}$/.test(accountInput)) {
        wx.showToast({ title: '账号格式不正确', icon: 'none' })
        return
      }
    }
    try {
      await meApi.changePassword({
        changePasswordRequest: {
          oldPassword: account?.passwordSet ? oldPassword : undefined,
          newPassword,
          confirmPassword,
          accountName: account?.accountNameSet ? undefined : accountInput,
        },
      })
      wx.showToast({ title: '密码已更新', icon: 'success' })
      this.closePasswordDialog()
      this.loadAccount()
    } catch (e) {
      showApiError(e)
    }
  },

  async submitAccountName() {
    const { accountNameInput, accountPasswordInput } = this.data
    if (!/^[a-zA-Z0-9_]{1,15}$/.test(accountNameInput)) {
      wx.showToast({ title: '账号格式不正确', icon: 'none' })
      return
    }
    try {
      await meApi.changeAccountName({
        changeAccountNameRequest: {
          newAccountName: accountNameInput,
          password: accountPasswordInput,
        },
      })
      wx.showToast({ title: '账号已更新', icon: 'success' })
      this.closeAccountDialog()
      this.loadAccount()
    } catch (e) {
      showApiError(e)
    }
  },

  onLogout() {
    clearToken()
    wx.reLaunch({ url: '/pages/home/home' })
  },

  openDeactivate() {
    this.setData({ showDeactivate: true, deactivateConfirm: '' })
  },

  closeDeactivate() {
    this.setData({ showDeactivate: false })
  },

  onDeactivateInput(e: WechatMiniprogram.CustomEvent) {
    this.setData({ deactivateConfirm: e.detail as string })
  },

  async submitDeactivate() {
    if (this.data.deactivateConfirm !== '确认注销') {
      wx.showToast({ title: '请输入「确认注销」', icon: 'none' })
      return
    }
    try {
      await meApi.deactivate()
      clearToken()
      wx.showToast({ title: '账号已注销', icon: 'success' })
      setTimeout(() => wx.reLaunch({ url: '/pages/home/home' }), 1500)
    } catch (e) {
      showApiError(e)
    }
  },
})
