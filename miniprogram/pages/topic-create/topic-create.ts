import { ensureLogin } from '../../api/auth'
import { meApi, topicApi } from '../../api/instances'
import { uploadImage } from '../../api/upload'
import { showApiError } from '../../utils/request'

const TOPIC_CREATE_LEVELS = new Set(['growth', 'creator', 'seasoned', 'master'])

Page({
  data: {
    title: '',
    description: '',
    durationDays: 14,
    coverUrl: '',
    rewardPool: 0,
    rewardTopN: 3,
    rewardRatio: '5:3:2',
    balance: 0,
    level: '',
    submitting: false,
    success: false,
    successMessage: '',
    costCreate: 20,
    totalCost: 20,
    canSubmit: false,
  },

  onLoad() {
    this.bootstrap()
  },

  async bootstrap() {
    try {
      await ensureLogin()
      const me = await meApi.getMe()
      const level = me.level || ''
      if (!TOPIC_CREATE_LEVELS.has(level)) {
        wx.showToast({ title: '达到成长等级后可发起话题', icon: 'none' })
        setTimeout(() => wx.navigateBack(), 1200)
        return
      }
      this.setData({
        balance: me.bookCoinBalance || 0,
        level,
      })
      this.recalc()
    } catch (e) {
      showApiError(e)
    }
  },

  onTitleInput(e: WechatMiniprogram.Input) {
    this.setData({ title: e.detail.value.slice(0, 20) })
    this.recalc()
  },

  onDescInput(e: WechatMiniprogram.Input) {
    this.setData({ description: e.detail.value.slice(0, 200) })
  },

  onDuration(e: WechatMiniprogram.TouchEvent) {
    this.setData({ durationDays: Number(e.currentTarget.dataset.days) })
  },

  onRewardPoolInput(e: WechatMiniprogram.Input) {
    const n = Math.max(0, Number(e.detail.value) || 0)
    this.setData({ rewardPool: n })
    this.recalc()
  },

  onTopN(e: WechatMiniprogram.TouchEvent) {
    const n = Number(e.currentTarget.dataset.n)
    this.setData({
      rewardTopN: n,
      rewardRatio: n === 5 ? '4:2:2:1:1' : '5:3:2',
    })
  },

  onRatioInput(e: WechatMiniprogram.Input) {
    this.setData({ rewardRatio: e.detail.value })
  },

  recalc() {
    const pool = this.data.rewardPool
    const poolCost = pool > 0 && pool < 50 ? 0 : pool
    const totalCost = 20 + (pool >= 50 ? pool : 0)
    const titleOk = this.data.title.trim().length > 0
    const poolOk = pool === 0 || pool >= 50
    const canSubmit = titleOk && poolOk && this.data.balance >= totalCost && !this.data.submitting
    this.setData({ totalCost, costCreate: 20, canSubmit })
    void poolCost
  },

  async chooseCover() {
    try {
      await ensureLogin()
      const choose = await wx.chooseMedia({
        count: 1,
        mediaType: ['image'],
        sourceType: ['album', 'camera'],
      })
      const path = choose.tempFiles[0]?.tempFilePath
      if (!path) return
      wx.showLoading({ title: '上传中' })
      const res = await uploadImage(path)
      this.setData({ coverUrl: res.url })
      wx.hideLoading()
    } catch (e) {
      wx.hideLoading()
      showApiError(e, '上传封面失败')
    }
  },

  clearCover() {
    this.setData({ coverUrl: '' })
  },

  goRecharge() {
    wx.showToast({ title: '书币充值即将上线', icon: 'none' })
  },

  async submit() {
    this.recalc()
    if (!this.data.title.trim()) {
      wx.showToast({ title: '请填写话题标题', icon: 'none' })
      return
    }
    if (this.data.rewardPool > 0 && this.data.rewardPool < 50) {
      wx.showToast({ title: '奖励池最低 50 书币', icon: 'none' })
      return
    }
    if (this.data.balance < this.data.totalCost) {
      wx.showToast({ title: '书币不足', icon: 'none' })
      return
    }
    this.setData({ submitting: true })
    try {
      const res = await topicApi.create({
        createTopicRequest: {
          title: this.data.title.trim(),
          description: this.data.description.trim() || undefined,
          durationDays: this.data.durationDays,
          coverUrl: this.data.coverUrl || undefined,
          rewardPoolAmount: this.data.rewardPool >= 50 ? this.data.rewardPool : 0,
          rewardTopN: this.data.rewardTopN,
          rewardRatio: this.data.rewardRatio,
        },
      })
      this.setData({
        success: true,
        successMessage: res.message || '话题已提交审核',
      })
    } catch (e) {
      showApiError(e, '发起话题失败')
    } finally {
      this.setData({ submitting: false })
    }
  },

  backToTopics() {
    wx.switchTab({ url: '/pages/topics/topics' })
  },

  goMyTopics() {
    wx.showToast({ title: '我发起的话题即将上线', icon: 'none' })
  },
})
