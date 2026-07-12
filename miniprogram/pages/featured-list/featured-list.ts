import { articleApi } from '../../api/instances'
import type { FeaturedArticleItemDto } from '../../api/generated/models/FeaturedArticleItemDto'
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

type FeaturedCard = FeaturedArticleItemDto & {
  publishedAt: string
  viewCount: number | string
}

Page({
  data: {
    loading: true,
    loadingMore: false,
    error: false,
    items: [] as FeaturedCard[],
    page: 1,
    total: 0,
    hasMore: true,
  },

  onLoad() {
    this.loadList(true)
  },

  async loadList(reset: boolean) {
    if (reset) {
      this.setData({ loading: true, error: false, page: 1, items: [], hasMore: true })
    } else {
      if (!this.data.hasMore || this.data.loadingMore) return
      this.setData({ loadingMore: true })
    }
    const page = reset ? 1 : this.data.page
    try {
      const res = await articleApi.featured({ page, size: 20 })
      const mapped: FeaturedCard[] = (res.items || []).map((item) => ({
        ...item,
        publishedAt: formatDate(item.publishedAt),
        viewCount: formatCount(item.viewCount),
      }))
      const items = reset ? mapped : [...this.data.items, ...mapped]
      const total = res.total || 0
      this.setData({
        items,
        total,
        page: page + 1,
        hasMore: items.length < total,
        error: false,
      })
    } catch (e) {
      if (reset) this.setData({ error: true, items: [] })
      showApiError(e, '加载精选失败')
    } finally {
      this.setData({ loading: false, loadingMore: false })
    }
  },

  loadMore() {
    this.loadList(false)
  },

  onTapCard(e: WechatMiniprogram.CustomEvent) {
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${e.detail.id}` })
  },

  onTapAuthor() {
    wx.showToast({ title: '个人主页即将上线', icon: 'none' })
  },

  retry() {
    this.loadList(true)
  },
})
