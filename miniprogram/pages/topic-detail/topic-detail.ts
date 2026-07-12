import { ensureLogin } from '../../api/auth'
import { topicApi } from '../../api/instances'
import type { TopicArticleItemDto } from '../../api/generated/models/TopicArticleItemDto'
import type { TopicDetailResponse } from '../../api/generated/models/TopicDetailResponse'
import { showApiError } from '../../utils/request'

function formatCount(n?: number): string {
  const v = n || 0
  if (v >= 10000) return `${(v / 10000).toFixed(1)}w`
  if (v >= 1000) return `${(v / 1000).toFixed(1)}k`
  return String(v)
}

function statusLabel(status?: string): string {
  if (status === 'ongoing') return '进行中'
  if (status === 'ended') return '已结束'
  return '即将开始'
}

function statusClass(status?: string): string {
  if (status === 'ongoing') return 'ongoing'
  if (status === 'ended') return 'ended'
  return 'upcoming'
}

function rankLabel(rank?: number | null): string {
  if (rank == null) return ''
  if (rank === 1) return '🥇'
  if (rank === 2) return '🥈'
  if (rank === 3) return '🥉'
  return String(rank)
}

Page({
  data: {
    id: 0,
    loading: true,
    topic: null as TopicDetailResponse | null,
    statusLabel: '',
    statusClass: '',
    dateHint: '',
    articleCountText: '0',
    viewCountText: '0',
    likeCountText: '0',
    descExpanded: false,
    showShare: false,
    sort: 'hot',
    articles: [] as Array<TopicArticleItemDto & { rankLabel: string; viewText: string; likeText: string }>,
    page: 1,
    hasMore: true,
    loadingMore: false,
  },

  onLoad(options: Record<string, string | undefined>) {
    const id = Number(options.id || 0)
    this.setData({ id })
    if (id > 0) {
      this.loadDetail()
      this.loadArticles(true)
    }
  },

  onShareAppMessage() {
    const topic = this.data.topic
    return {
      title: topic?.title || '话题',
      path: `/pages/topic-detail/topic-detail?id=${this.data.id}`,
    }
  },

  async loadDetail() {
    this.setData({ loading: true })
    try {
      const topic = await topicApi.detail({ id: this.data.id })
      const end = topic.endAt ? topic.endAt.slice(0, 10) : ''
      const start = topic.startAt ? topic.startAt.slice(0, 10) : ''
      let dateHint = ''
      if (topic.status === 'ongoing' && end) dateHint = `截止 ${end}`
      else if (topic.status === 'ended' && end) dateHint = `结束于 ${end}`
      else if (start) dateHint = `开始 ${start}`
      this.setData({
        topic,
        statusLabel: statusLabel(topic.status),
        statusClass: statusClass(topic.status),
        dateHint,
        articleCountText: formatCount(topic.articleCount),
        viewCountText: formatCount(topic.viewCount),
        likeCountText: formatCount(topic.likeCount),
      })
    } catch (e) {
      showApiError(e, '加载话题失败')
    } finally {
      this.setData({ loading: false })
    }
  },

  async loadArticles(reset: boolean) {
    if (!reset && (!this.data.hasMore || this.data.loadingMore)) return
    if (reset) {
      this.setData({ page: 1, articles: [], hasMore: true })
    } else {
      this.setData({ loadingMore: true })
    }
    const page = reset ? 1 : this.data.page
    try {
      const res = await topicApi.articles({
        id: this.data.id,
        sort: this.data.sort,
        page,
        size: 20,
      })
      const mapped = (res.items || []).map((item) => ({
        ...item,
        rankLabel: rankLabel(item.rank),
        viewText: formatCount(item.viewCount),
        likeText: formatCount(item.likeCount),
      }))
      const articles = reset ? mapped : [...this.data.articles, ...mapped]
      const total = res.total || 0
      this.setData({
        articles,
        page: page + 1,
        hasMore: articles.length < total,
      })
    } catch (e) {
      showApiError(e, '加载文章失败')
    } finally {
      this.setData({ loadingMore: false })
    }
  },

  onSortTab(e: WechatMiniprogram.TouchEvent) {
    const sort = e.currentTarget.dataset.sort as string
    if (sort === this.data.sort) return
    this.setData({ sort })
    this.loadArticles(true)
  },

  toggleDesc() {
    this.setData({ descExpanded: !this.data.descExpanded })
  },

  openShare() {
    this.setData({ showShare: true })
  },

  closeShare() {
    this.setData({ showShare: false })
  },

  onShareMoment() {
    wx.showToast({ title: '朋友圈分享即将上线', icon: 'none' })
  },

  onShareImage() {
    wx.showToast({ title: '生成图片即将上线', icon: 'none' })
  },

  async goWrite() {
    try {
      await ensureLogin()
      const title = encodeURIComponent(this.data.topic?.title || '')
      wx.navigateTo({
        url: `/pages/write/write?topic_id=${this.data.id}&topic_title=${title}&from=topics`,
      })
    } catch (e) {
      showApiError(e)
    }
  },

  goArticle(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${id}` })
  },

  onReachBottom() {
    this.loadArticles(false)
  },
})
