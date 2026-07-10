Page({
  data: { title: '话题详情', id: 0 },
  onLoad(q: Record<string, string>) {
    this.setData({ id: Number(q.id || 0) })
  },
  goBack() { wx.navigateBack({ fail: () => wx.switchTab({ url: '/pages/topics/topics' }) }) },
})