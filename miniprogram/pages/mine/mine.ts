import { clearToken, ensureLogin } from '../../api/auth'
import { meApi } from '../../api/instances'
import type { MeResponse } from '../../api/generated/models/MeResponse'
import type { MyArticleItem } from '../../api/generated/models/MyArticleItem'
import { STATUS_COLORS, STATUS_LABELS, showApiError } from '../../utils/request'

const STATUS_TABS = [
  { key: '', label: '全部' },
  { key: 'draft', label: '草稿' },
  { key: 'reviewing', label: '审核中' },
  { key: 'published', label: '已发布' },
  { key: 'rejected', label: '已拒绝' },
]

Page({
  data: {
    me: null as MeResponse | null,
    statusTabs: STATUS_TABS,
    activeStatus: '',
    articles: [] as MyArticleItem[],
    loading: false,
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 4 })
    }
    this.refresh()
  },

  async refresh() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const me = await meApi.getMe()
      this.setData({ me })
      await this.loadArticles()
    } catch (e) {
      showApiError(e, '加载失败')
    } finally {
      this.setData({ loading: false })
    }
  },

  async loadArticles() {
    const res = await meApi.listArticles({
      status: this.data.activeStatus || undefined,
    })
    const articles = (res.items || []).map((item) => ({
      ...item,
      updatedAtLabel: item.updatedAt
        ? item.updatedAt.toLocaleString('zh-CN', { hour12: false })
        : '',
    }))
    this.setData({ articles })
  },

  onTabChange(e: WechatMiniprogram.TouchEvent) {
    const key = e.currentTarget.dataset.key as string
    this.setData({ activeStatus: key })
    this.loadArticles().catch((err) => showApiError(err))
  },

  goNotifications() {
    wx.navigateTo({ url: '/pages/notifications/notifications' })
  },

  goSettings() {
    wx.navigateTo({ url: '/pages/settings/settings' })
  },

  onGetUserProfile() {
    wx.getUserProfile({
      desc: '用于展示头像和昵称',
      success: async (res) => {
        try {
          const user = res.userInfo
          await meApi.updateWechatProfile({
            wechatProfileRequest: {
              nickname: user.nickName,
              avatarUrl: user.avatarUrl,
            },
          })
          wx.showToast({ title: '资料已更新', icon: 'success' })
          this.refresh()
        } catch (e) {
          showApiError(e)
        }
      },
      fail: () => wx.showToast({ title: '已取消授权', icon: 'none' }),
    })
  },

  goWrite() {
    wx.navigateTo({ url: '/pages/write/write?from=mine' })
  },

  goDetail(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${id}` })
  },

  onLogout() {
    clearToken()
    ensureLogin()
      .then(() => {
        wx.showToast({ title: '已重新登录', icon: 'success' })
        this.refresh()
      })
      .catch((err) => showApiError(err))
  },

  statusLabel(status: string) {
    return STATUS_LABELS[status] || status
  },

  statusColor(status: string) {
    return STATUS_COLORS[status] || '#909399'
  },
})
