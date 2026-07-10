import { testEssayApi } from '../../api/instances'
import type { TestEssayDto } from '../../api/generated/models/TestEssayDto'

Page({
  data: {
    list: [] as TestEssayDto[],
  },

  onGet() {
    testEssayApi
      .getList({
        testRequest: {
          categoryId: 0,
          authorName: '',
        },
      })
      .then((res: TestEssayDto[]) => {
        this.setData({ list: res })
      })
      .catch((err: unknown) => {
        console.error('请求失败', err)
        wx.showToast({ title: '请求失败', icon: 'error' })
      })
  },

  onClear() {
    this.setData({ list: [] })
  },
})
