import { categoryApi } from '../../api/instances'
import type { CategoryNodeDto } from '../../api/generated/models/CategoryNodeDto'
import { showApiError } from '../../utils/request'

Page({
  data: {
    categories: [] as CategoryNodeDto[],
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 3 })
    }
    this.loadTree()
  },

  async loadTree() {
    try {
      const res = await categoryApi.tree()
      this.setData({ categories: res.categories || [] })
    } catch (e) {
      showApiError(e)
    }
  },
})
