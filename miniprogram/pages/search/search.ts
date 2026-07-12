import { searchApi } from '../../api/instances'
import type { SearchArticleItemDto } from '../../api/generated/models/SearchArticleItemDto'
import type { SearchUserItemDto } from '../../api/generated/models/SearchUserItemDto'
import type { TopicItemDto } from '../../api/generated/models/TopicItemDto'
import { showApiError } from '../../utils/request'

const TYPE_OPTIONS = [
  { value: 'title', label: '标题' },
  { value: 'author', label: '作者' },
  { value: 'tag', label: '标签' },
  { value: 'topic', label: '话题' },
]

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

Page({
  data: {
    q: '',
    type: 'title',
    typeLabel: '标题',
    typeOptions: TYPE_OPTIONS,
    showTypePicker: false,
    loading: false,
    error: false,
    searched: false,
    total: 0,
    page: 1,
    hasMore: false,
    loadingMore: false,
    articles: [] as Array<SearchArticleItemDto & { publishedAt: string; viewCount: number | string }>,
    users: [] as SearchUserItemDto[],
    topics: [] as Array<TopicItemDto & { metaText: string }>,
  },

  onLoad(options: Record<string, string | undefined>) {
    const type = options.type || 'title'
    const q = options.q ? decodeURIComponent(options.q) : ''
    const opt = TYPE_OPTIONS.find((t) => t.value === type) || TYPE_OPTIONS[0]
    this.setData({
      type: opt.value,
      typeLabel: opt.label,
      q,
    })
    if (q) {
      this.doSearch(true)
    }
  },

  onQInput(e: WechatMiniprogram.Input) {
    this.setData({ q: e.detail.value })
  },

  toggleTypePicker() {
    this.setData({ showTypePicker: !this.data.showTypePicker })
  },

  onSelectType(e: WechatMiniprogram.TouchEvent) {
    const type = e.currentTarget.dataset.type as string
    const opt = TYPE_OPTIONS.find((t) => t.value === type) || TYPE_OPTIONS[0]
    this.setData({
      type: opt.value,
      typeLabel: opt.label,
      showTypePicker: false,
    })
    if (this.data.q.trim()) {
      this.doSearch(true)
    }
  },

  onSearchConfirm() {
    this.doSearch(true)
  },

  async doSearch(reset: boolean) {
    const q = this.data.q.trim()
    if (!q) {
      wx.showToast({ title: '请输入关键词', icon: 'none' })
      return
    }
    if (reset) {
      this.setData({
        loading: true,
        error: false,
        searched: true,
        page: 1,
        articles: [],
        users: [],
        topics: [],
        hasMore: false,
      })
    } else {
      if (!this.data.hasMore || this.data.loadingMore) return
      this.setData({ loadingMore: true })
    }
    const page = reset ? 1 : this.data.page
    try {
      const res = await searchApi.search1({
        q,
        type: this.data.type,
        page,
        size: 20,
      })
      const total = res.total || 0
      if (this.data.type === 'author') {
        const users = reset ? res.users || [] : [...this.data.users, ...(res.users || [])]
        this.setData({
          users,
          total,
          page: page + 1,
          hasMore: users.length < total,
        })
      } else if (this.data.type === 'topic') {
        const mapped = (res.topics || []).map((t) => ({
          ...t,
          metaText:
            t.status === 'ended'
              ? `✅ 已结束 · 共${t.articleCount || 0}篇`
              : `📝 ${t.articleCount || 0}篇  ⏳ 还剩${t.daysRemaining ?? '-'}天`,
        }))
        const topics = reset ? mapped : [...this.data.topics, ...mapped]
        this.setData({
          topics,
          total,
          page: page + 1,
          hasMore: topics.length < total,
        })
      } else {
        const mapped = (res.articles || []).map((a) => ({
          ...a,
          publishedAt: formatDate(a.publishedAt),
          viewCount: formatCount(a.viewCount),
        }))
        const articles = reset ? mapped : [...this.data.articles, ...mapped]
        this.setData({
          articles,
          total,
          page: page + 1,
          hasMore: articles.length < total,
        })
      }
      this.setData({ error: false })
    } catch (e) {
      if (reset) this.setData({ error: true })
      showApiError(e, '搜索失败')
    } finally {
      this.setData({ loading: false, loadingMore: false })
    }
  },

  loadMore() {
    this.doSearch(false)
  },

  onTapCard(e: WechatMiniprogram.CustomEvent) {
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${e.detail.id}` })
  },

  onTapAuthor() {
    wx.showToast({ title: '个人主页即将上线', icon: 'none' })
  },

  onTapUser() {
    wx.showToast({ title: '个人主页即将上线', icon: 'none' })
  },

  goTopic(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/topic-detail/topic-detail?id=${id}` })
  },

  goHome() {
    wx.switchTab({ url: '/pages/home/home' })
  },

  retry() {
    this.doSearch(true)
  },
})
