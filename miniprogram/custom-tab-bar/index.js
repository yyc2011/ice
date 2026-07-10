const TAB_FROM = {
  0: 'home',
  1: 'topics',
  3: 'categories',
  4: 'mine',
}

Component({
  data: {
    selected: 0,
    color: '#666666',
    selectedColor: '#1989fa',
    tabs: [
      {
        index: 0,
        isWrite: false,
        pagePath: '/pages/home/home',
        text: '首页',
        iconPath: '/assets/tab/home.png',
        selectedIconPath: '/assets/tab/home-active.png',
      },
      {
        index: 1,
        isWrite: false,
        pagePath: '/pages/topics/topics',
        text: '话题',
        iconPath: '/assets/tab/topics.png',
        selectedIconPath: '/assets/tab/topics-active.png',
      },
      {
        index: 2,
        isWrite: true,
      },
      {
        index: 3,
        isWrite: false,
        pagePath: '/pages/categories/categories',
        text: '分类',
        iconPath: '/assets/tab/categories.png',
        selectedIconPath: '/assets/tab/categories-active.png',
      },
      {
        index: 4,
        isWrite: false,
        pagePath: '/pages/mine/mine',
        text: '我的',
        iconPath: '/assets/tab/mine.png',
        selectedIconPath: '/assets/tab/mine-active.png',
      },
    ],
  },

  methods: {
    onTabTap(e) {
      const index = e.currentTarget.dataset.index
      const isWrite = e.currentTarget.dataset.isWrite
      if (isWrite) {
        const from = TAB_FROM[this.data.selected] || 'home'
        wx.navigateTo({ url: `/pages/write/write?from=${from}` })
        return
      }
      const tab = this.data.tabs.find((item) => item.index === index)
      if (!tab || !tab.pagePath) return
      wx.switchTab({ url: tab.pagePath })
      this.setData({ selected: index })
    },
  },
})
