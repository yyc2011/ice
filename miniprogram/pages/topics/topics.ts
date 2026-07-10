import { ensureLogin } from '../../api/auth'
import { topicApi } from '../../api/instances'
import type { TopicItemDto } from '../../api/generated/models/TopicItemDto'
import { showApiError } from '../../utils/request'

Page({
  data: {
    loading: true,
    ongoing: [] as TopicItemDto[],
    upcoming: [] as TopicItemDto[],
    upcomingExpanded: false,
    historicalTab: 'today',
    historical: [] as TopicItemDto[],
    randomTopics: [] as TopicItemDto[],
    randomPage: 1,
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 1 })
    }
    this.loadAll()
  },

  async loadAll() {
    this.setData({ loading: true })
    try {
      await ensureLogin()
      const [ongoingRes, upcomingRes, historicalRes, randomRes] = await Promise.all([
        topicApi.list({ status: 'ongoing', size: 10 }),
        topicApi.list({ status: 'upcoming', size: 20 }),
        topicApi.list({ mode: 'historical', period: this.data.historicalTab, size: 10 }),
        topicApi.list({ mode: 'random', size: 8 }),
      ])
      this.setData({
        ongoing: ongoingRes.items || [],
        upcoming: upcomingRes.items || [],
        historical: historicalRes.items || [],
        randomTopics: randomRes.items || [],
      })
    } catch (e) {
      showApiError(e, '话题加载失败')
    } finally {
      this.setData({ loading: false })
    }
  },

  onPullDownRefresh() {
    this.loadAll().finally(() => wx.stopPullDownRefresh())
  },

  toggleUpcoming() {
    this.setData({ upcomingExpanded: !this.data.upcomingExpanded })
  },

  async onHistoricalTab(e: WechatMiniprogram.TouchEvent) {
    const tab = e.currentTarget.dataset.tab as string
    this.setData({ historicalTab: tab })
    try {
      const res = await topicApi.list({ mode: 'historical', period: tab, size: 10 })
      this.setData({ historical: res.items || [] })
    } catch (err) {
      showApiError(err)
    }
  },

  async loadMoreRandom() {
    try {
      const res = await topicApi.list({ mode: 'random', size: 8 })
      this.setData({
        randomTopics: [...this.data.randomTopics, ...(res.items || [])],
        randomPage: this.data.randomPage + 1,
      })
    } catch (err) {
      showApiError(err)
    }
  },

  goSearch() {
    wx.navigateTo({ url: '/pages/search/search?type=topic' })
  },

  goTopicDetail(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: `/pages/topic-detail/topic-detail?id=${id}` })
  },

  goWriteWithTopic(e: WechatMiniprogram.TouchEvent) {
    const id = e.currentTarget.dataset.id
    const title = e.currentTarget.dataset.title
    wx.navigateTo({
      url: `/pages/write/write?topic_id=${id}&topic_title=${encodeURIComponent(title)}`,
    })
  },

  goCreateTopic() {
    wx.navigateTo({ url: '/pages/topic-create/topic-create' })
  },
})
