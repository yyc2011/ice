import { clearToken, ensureLogin } from '../../api/auth'
import { meApi, notificationApi, articleApi } from '../../api/instances'
import type { NotificationItemDto } from '../../api/generated/models/NotificationItemDto'
import { showApiError } from '../../utils/request'

Page({
  data: {
    items: [] as NotificationItemDto[],
    bookCoinBalance: 0,
    showAppeal: false,
    appealType: 'ai' as 'ai' | 'manual',
    appealArticleId: 0,
    appealText: '',
    expandedId: 0,
  },

  onShow() {
    this.loadData()
  },

  async loadData() {
    try {
      const [me, notifications] = await Promise.all([
        meApi.getMe(),
        notificationApi.listNotifications(),
      ])
      this.setData({
        items: (notifications.items || []).map((item) => ({
          ...item,
          createdAtLabel: item.createdAt
            ? item.createdAt.toLocaleString('zh-CN', { hour12: false })
            : '',
        })),
        bookCoinBalance: me.bookCoinBalance || 0,
      })
      await notificationApi.markRead({})
    } catch (e) {
      showApiError(e)
    }
  },

  onTapItem(e: WechatMiniprogram.TouchEvent) {
    const item = e.currentTarget.dataset.item as NotificationItemDto
    if (item.type === 'system') {
      this.setData({ expandedId: this.data.expandedId === item.id ? 0 : (item.id || 0) })
      return
    }
    if (item.refType === 'article' && item.refId) {
      wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${item.refId}` })
    }
  },

  openAppeal(e: WechatMiniprogram.TouchEvent) {
    const type = e.currentTarget.dataset.type as 'ai' | 'manual'
    const articleId = Number(e.currentTarget.dataset.articleId)
    const cost = type === 'ai' ? 10 : 30
    if (this.data.bookCoinBalance < cost) {
      wx.showToast({ title: '书币不足', icon: 'none' })
      return
    }
    this.setData({
      showAppeal: true,
      appealType: type,
      appealArticleId: articleId,
      appealText: '',
    })
  },

  closeAppeal() {
    this.setData({ showAppeal: false })
  },

  onAppealInput(e: WechatMiniprogram.Input) {
    this.setData({ appealText: e.detail.value })
  },

  async submitAppeal() {
    try {
      const res = await articleApi.reviewAppeal({
        id: this.data.appealArticleId,
        reviewAppealRequest: {
          appealType: this.data.appealType,
          appealText: this.data.appealText,
        },
      })
      wx.showToast({ title: res.message || '已提交复审', icon: 'success' })
      this.setData({ showAppeal: false })
      this.loadData()
    } catch (e) {
      showApiError(e)
    }
  },

  goEdit(e: WechatMiniprogram.TouchEvent) {
    const articleId = Number(e.currentTarget.dataset.articleId)
    wx.navigateTo({ url: `/pages/write/write?articleId=${articleId}&from=notifications` })
  },
})
