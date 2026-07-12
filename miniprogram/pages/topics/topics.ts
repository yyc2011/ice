import { ensureLogin } from '../../api/auth'
import { meApi, topicApi } from '../../api/instances'
import type { TopicItemDto } from '../../api/generated/models/TopicItemDto'
import { showApiError } from '../../utils/request'

const TOPIC_CREATE_LEVELS = new Set(['growth', 'creator', 'seasoned', 'master'])

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

  onTopicCard(e: WechatMiniprogram.CustomEvent) {
    const id = e.detail.id
    if (id) {
      wx.navigateTo({ url: `/pages/topic-detail/topic-detail?id=${id}` })
    }
  },

  onTopicJoin(e: WechatMiniprogram.CustomEvent) {
    const { id, title } = e.detail as { id?: number; title?: string }
    if (!id) return
    wx.navigateTo({
      url: `/pages/write/write?topic_id=${id}&topic_title=${encodeURIComponent(title || '')}&from=topics`,
    })
  },

  async goCreateTopic() {
    try {
      await ensureLogin()
      const me = await meApi.getMe()
      if (!TOPIC_CREATE_LEVELS.has(me.level || '')) {
        wx.showToast({ title: '达到成长等级后可发起话题', icon: 'none' })
        return
      }
      wx.navigateTo({ url: '/pages/topic-create/topic-create' })
    } catch (e) {
      showApiError(e)
    }
  },
})
