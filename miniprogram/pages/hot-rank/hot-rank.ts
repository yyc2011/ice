import { rankingApi } from '../../api/instances'
import type { HotRankingItemDto } from '../../api/generated/models/HotRankingItemDto'
import { showApiError } from '../../utils/request'

function formatDate(iso?: string): string {
  if (!iso) return ''
  const d = new Date(iso)
  if (Number.isNaN(d.getTime())) return iso.slice(0, 10)
  const m = `${d.getMonth() + 1}`.padStart(2, '0')
  const day = `${d.getDate()}`.padStart(2, '0')
  return `${d.getFullYear()}-${m}-${day}`
}

function formatCount(n?: number): string {
  const v = n || 0
  if (v >= 10000) return `${(v / 10000).toFixed(1)}w`
  if (v >= 1000) return `${(v / 1000).toFixed(1)}k`
  return String(v)
}

type HotCard = HotRankingItemDto & {
  publishedAt: string
  viewCount: number | string
  likeCount: number | string
  commentCount: number | string
}

Page({
  data: {
    window: '24h',
    loading: true,
    error: false,
    items: [] as HotCard[],
  },

  onLoad() {
    this.loadList()
  },

  onWindowTab(e: WechatMiniprogram.TouchEvent) {
    const window = e.currentTarget.dataset.window as string
    if (window === this.data.window) return
    this.setData({ window })
    this.loadList()
  },

  async loadList() {
    this.setData({ loading: true, error: false })
    try {
      const res = await rankingApi.hot({ window: this.data.window, size: 50 })
      const items: HotCard[] = (res.items || []).map((item) => ({
        ...item,
        publishedAt: formatDate(item.publishedAt),
        viewCount: formatCount(item.viewCount),
        likeCount: formatCount(item.likeCount),
        commentCount: formatCount(item.commentCount),
      }))
      this.setData({ items })
    } catch (e) {
      this.setData({ error: true, items: [] })
      showApiError(e, '加载热榜失败')
    } finally {
      this.setData({ loading: false })
    }
  },

  onTapCard(e: WechatMiniprogram.CustomEvent) {
    const id = e.detail.id
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${id}` })
  },

  onTapAuthor() {
    wx.showToast({ title: '个人主页即将上线', icon: 'none' })
  },

  goBack() {
    wx.navigateBack({ fail: () => wx.switchTab({ url: '/pages/home/home' }) })
  },
})
