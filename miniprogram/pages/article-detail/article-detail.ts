import { articleApi } from '../../api/instances'
import type { ArticleDetailResponse } from '../../api/generated/models/ArticleDetailResponse'
import { STATUS_LABELS, showApiError } from '../../utils/request'

Page({
  data: {
    article: null as ArticleDetailResponse | null,
    articleId: 0,
  },

  onLoad(options: Record<string, string | undefined>) {
    const id = Number(options.id || 0)
    this.setData({ articleId: id })
    if (id > 0) {
      this.loadArticle(id)
    }
  },

  async loadArticle(id: number) {
    try {
      wx.showLoading({ title: '加载中' })
      const article = await articleApi.getArticle({ id })
      this.setData({ article })
    } catch (e) {
      showApiError(e)
    } finally {
      wx.hideLoading()
    }
  },

  goEdit() {
    wx.navigateTo({ url: `/pages/write/write?articleId=${this.data.articleId}&from=article-detail` })
  },

  statusLabel(status: string) {
    return STATUS_LABELS[status] || status
  },
})
