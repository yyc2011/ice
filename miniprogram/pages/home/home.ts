import { ensureLogin } from '../../api/auth'
import {
  articleApi,
  categoryApi,
  rankingApi,
  topicApi,
} from '../../api/instances'
import type { CategoryPreviewItemDto } from '../../api/generated/models/CategoryPreviewItemDto'
import type { FeaturedArticleItemDto } from '../../api/generated/models/FeaturedArticleItemDto'
import type { HotRankingItemDto } from '../../api/generated/models/HotRankingItemDto'
import type { TopicItemDto } from '../../api/generated/models/TopicItemDto'
import { showApiError } from '../../utils/request'

Page({
  data: {
    loading: true,
    topics: [] as TopicItemDto[],
    hotItems: [] as HotRankingItemDto[],
    featured: [] as FeaturedArticleItemDto[],
    categories: [] as CategoryPreviewItemDto[],
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 0 })
    }
    this.loadData()
  },

  async loadData() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const [topicsRes, hotRes, featuredRes, previewRes] = await Promise.all([
        topicApi.ongoing({ size: 5 }),
        rankingApi.hot({ window: '24h', size: 5 }),
        articleApi.featured({ size: 10 }),
        categoryApi.preview(),
      ])
      this.setData({
        topics: topicsRes.items || [],
        hotItems: hotRes.items || [],
        featured: featuredRes.items || [],
        categories: previewRes.categories || [],
      })
    } catch (e) {
      showApiError(e, '首页加载失败')
    } finally {
      this.setData({ loading: false })
    }
  },

  onPullDownRefresh() {
    this.loadData().finally(() => wx.stopPullDownRefresh())
  },

  goTopics() {
    wx.switchTab({ url: '/pages/topics/topics' })
  },

  goTopicDetail(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/topic-detail/topic-detail?id=${id}` })
  },

  goHotRank() {
    wx.navigateTo({ url: '/pages/hot-rank/hot-rank' })
  },

  goFeaturedList() {
    wx.navigateTo({ url: '/pages/featured-list/featured-list' })
  },

  goArticle(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/article-detail/article-detail?id=${id}` })
  },

  goCategories() {
    wx.switchTab({ url: '/pages/categories/categories' })
  },

  goSearch() {
    wx.navigateTo({ url: '/pages/search/search' })
  },

  formatCount(n: number) {
    if (n >= 10000) return `${(n / 10000).toFixed(1)}w`
    if (n >= 1000) return `${(n / 1000).toFixed(1)}k`
    return String(n)
  },
})
