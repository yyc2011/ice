Page({
  data: { title: '热榜' },
  onLoad() {},
  goBack() {
    wx.navigateBack({ fail: () => wx.switchTab({ url: '/pages/home/home' }) })
  },
})
