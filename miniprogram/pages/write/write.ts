import { ensureLogin } from '../../api/auth'
import {
  articleApi,
  categoryApi,
  meApi,
  tagApi,
} from '../../api/instances'
import type { CategoryNodeDto } from '../../api/generated/models/CategoryNodeDto'
import type { TagItemDto } from '../../api/generated/models/TagItemDto'
import { showApiError } from '../../utils/request'

interface CategoryPickerItem {
  text: string
  id?: number
  children: { text: string; id: number }[]
}

const TAB_FROM_PAGES: Record<string, string> = {
  home: '/pages/home/home',
  topics: '/pages/topics/topics',
  categories: '/pages/categories/categories',
  mine: '/pages/mine/mine',
}

function toCategoryPickerItems(tree: CategoryNodeDto[]): CategoryPickerItem[] {
  return tree.map((cat) => ({
    text: cat.name || '',
    id: cat.id,
    children: (cat.children || []).map((sub) => ({ text: sub.name || '', id: sub.id || 0 })),
  }))
}

function findCategoryPickerState(
  items: CategoryPickerItem[],
  categoryId: number | null,
): { mainIndex: number; activeId: number | null } {
  if (categoryId == null || items.length === 0) {
    return { mainIndex: 0, activeId: null }
  }
  for (let i = 0; i < items.length; i++) {
    const sub = items[i].children.find((c) => c.id === categoryId)
    if (sub) {
      return { mainIndex: i, activeId: categoryId }
    }
  }
  return { mainIndex: 0, activeId: null }
}

function readSearchValue(detail: unknown): string {
  if (typeof detail === 'string') return detail
  if (detail && typeof detail === 'object' && 'value' in detail) {
    return String((detail as { value?: string }).value ?? '')
  }
  return ''
}

const TAG_SEARCH_DEBOUNCE_MS = 300
let tagSearchDebounceTimer: ReturnType<typeof setTimeout> | null = null

Page({
  data: {
    statusBarHeight: 20,
    navPaddingRight: 96,
    from: 'home',
    articleId: 0 as number,
    title: '',
    content: '',
    wordCount: 0,
    wordLimit: 800,
    selectedTags: [] as TagItemDto[],
    tagSearch: '',
    tagResults: [] as TagItemDto[],
    tagSearching: false,
    showCreateTag: false,
    showSettings: false,
    showCategoryPicker: false,
    categoryTree: [] as CategoryNodeDto[],
    categoryPickerItems: [] as CategoryPickerItem[],
    categoryMainIndex: 0,
    categoryPickerActiveId: null as number | null,
    categoryPickerHeight: 400,
    selectedCategoryId: null as number | null,
    selectedCategoryLabel: '',
    topicId: null as number | null,
    topicTitle: '',
    saving: false,
    publishing: false,
  },

  onLoad(options: Record<string, string | undefined>) {
    const sys = wx.getSystemInfoSync()
    const menuRect = wx.getMenuButtonBoundingClientRect()
    const articleId = options.articleId ? Number(options.articleId) : 0
    const from = options.from || 'home'
    const topicId = options.topic_id ? Number(options.topic_id) : null
    const topicTitle = options.topic_title ? decodeURIComponent(options.topic_title) : ''
    this.setData({
      statusBarHeight: sys.statusBarHeight || 20,
      navPaddingRight: sys.windowWidth - menuRect.left + 8,
      categoryPickerHeight: Math.floor(sys.windowHeight * 0.45),
      articleId,
      from,
      topicId: topicId && topicId > 0 ? topicId : null,
      topicTitle,
    })
    this.loadCategories()
    this.loadWordLimit()
    if (articleId > 0) {
      this.loadArticle(articleId)
    }
  },

  async loadWordLimit() {
    try {
      await ensureLogin()
      const me = await meApi.getMe()
      this.setData({ wordLimit: me.wordLimit || 800 })
    } catch (e) {
      showApiError(e, '加载用户信息失败')
    }
  },

  async loadCategories() {
    try {
      const res = await categoryApi.tree()
      const categoryTree = res.categories || []
      this.setData({
        categoryTree,
        categoryPickerItems: toCategoryPickerItems(categoryTree),
      })
    } catch (e) {
      showApiError(e, '加载分类失败')
    }
  },

  async loadArticle(id: number) {
    try {
      wx.showLoading({ title: '加载中' })
      const article = await articleApi.getArticle({ id })
      this.setData({
        title: article.title || '',
        content: article.content || '',
        wordCount: (article.content || '').length,
        selectedTags: article.tags || [],
      })
    } catch (e) {
      showApiError(e, '加载文章失败')
    } finally {
      wx.hideLoading()
    }
  },

  onTitleInput(e: WechatMiniprogram.Input) {
    this.setData({ title: e.detail.value })
  },

  onContentInput(e: WechatMiniprogram.Input) {
    const content = e.detail.value
    this.setData({ content, wordCount: content.length })
  },

  onTagSearchChange(e: WechatMiniprogram.CustomEvent) {
    const query = readSearchValue(e.detail)
    this.setData({ tagSearch: query, showCreateTag: false })
    if (tagSearchDebounceTimer) {
      clearTimeout(tagSearchDebounceTimer)
    }
    tagSearchDebounceTimer = setTimeout(() => {
      tagSearchDebounceTimer = null
      void this.searchTags(query)
    }, TAG_SEARCH_DEBOUNCE_MS)
  },

  async onTagSearch(e?: WechatMiniprogram.CustomEvent) {
    const query = e ? readSearchValue(e.detail) : this.data.tagSearch
    if (e) {
      this.setData({ tagSearch: query })
    }
    if (tagSearchDebounceTimer) {
      clearTimeout(tagSearchDebounceTimer)
      tagSearchDebounceTimer = null
    }
    await this.searchTags(query)
  },

  async searchTags(query: string) {
    const trimmed = query.trim()
    this.setData({ tagSearching: true, showCreateTag: false })
    try {
      const res = await tagApi.search({ q: trimmed })
      const selectedIds = new Set(this.data.selectedTags.map((t) => t.id))
      const tagResults = (res.items || []).filter((t) => t.id && !selectedIds.has(t.id))
      this.setData({
        tagResults,
        tagSearching: false,
        showCreateTag: trimmed.length > 0 && tagResults.length === 0,
      })
    } catch (err) {
      this.setData({ tagSearching: false, showCreateTag: false })
      showApiError(err)
    }
  },

  onSelectTag(e: WechatMiniprogram.TouchEvent) {
    const id = Number(e.currentTarget.dataset.id)
    const name = e.currentTarget.dataset.name as string
    const selected = [...this.data.selectedTags]
    if (selected.some((t) => t.id === id)) return
    if (selected.length >= 5) {
      wx.showToast({ title: '最多 5 个标签', icon: 'none' })
      return
    }
    selected.push({ id, name })
    this.setData({ selectedTags: selected })
    void this.searchTags(this.data.tagSearch)
  },

  onRemoveTag(e: WechatMiniprogram.TouchEvent) {
    const id = Number(e.currentTarget.dataset.id)
    this.setData({
      selectedTags: this.data.selectedTags.filter((t) => t.id !== id),
    })
    void this.searchTags(this.data.tagSearch)
  },

  async onCreateTag() {
    const name = this.data.tagSearch.trim()
    if (!name) return
    try {
      await ensureLogin()
      const tag = await tagApi.create1({ createTagRequest: { name } })
      const selected = [...this.data.selectedTags]
      if (tag.id && !selected.some((t) => t.id === tag.id) && selected.length < 5) {
        selected.push(tag)
      }
      this.setData({
        selectedTags: selected,
        tagSearch: '',
        tagResults: [],
        showCreateTag: false,
      })
    } catch (e) {
      showApiError(e)
    }
  },

  openSettings() {
    this.setData({ showSettings: true })
    void this.searchTags(this.data.tagSearch)
  },

  closeSettings() {
    this.setData({ showSettings: false })
  },

  openCategoryPicker() {
    const { categoryPickerItems, selectedCategoryId } = this.data
    const { mainIndex, activeId } = findCategoryPickerState(
      categoryPickerItems,
      selectedCategoryId,
    )
    this.setData({
      showCategoryPicker: true,
      categoryMainIndex: mainIndex,
      categoryPickerActiveId: activeId,
    })
  },

  closeCategoryPicker() {
    this.setData({ showCategoryPicker: false })
  },

  onCategoryNavClick(e: WechatMiniprogram.CustomEvent) {
    const index = (e.detail as { index: number }).index
    this.setData({
      categoryMainIndex: index,
      categoryPickerActiveId: null,
    })
  },

  onCategoryItemClick(e: WechatMiniprogram.CustomEvent) {
    const item = e.detail as { id: number }
    this.setData({ categoryPickerActiveId: item.id })
  },

  onCategoryConfirm() {
    const { categoryPickerItems, categoryMainIndex, categoryPickerActiveId } = this.data
    if (!categoryPickerActiveId) {
      wx.showToast({ title: '请选择小类', icon: 'none' })
      return
    }
    const main = categoryPickerItems[categoryMainIndex]
    const sub = main?.children.find((c) => c.id === categoryPickerActiveId)
    if (!main || !sub) {
      wx.showToast({ title: '请选择小类', icon: 'none' })
      return
    }
    this.setData({
      selectedCategoryId: sub.id,
      selectedCategoryLabel: `${main.text} > ${sub.text}`,
      showCategoryPicker: false,
    })
  },

  clearCategory() {
    this.setData({
      selectedCategoryId: null,
      selectedCategoryLabel: '',
      categoryPickerActiveId: null,
    })
  },

  buildDraftBody() {
    return {
      title: this.data.title.trim(),
      content: this.data.content.trim(),
      tagIds: this.data.selectedTags.map((t) => t.id!),
      categoryId: this.data.selectedCategoryId ?? undefined,
      topicId: this.data.topicId ?? undefined,
      coverUrl: undefined,
      imageUrls: [],
    }
  },

  hasContent() {
    return Boolean(this.data.content.trim())
  },

  validateDraftContent() {
    if (!this.data.title.trim()) {
      wx.showToast({ title: '请填写标题', icon: 'none' })
      return false
    }
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请填写正文', icon: 'none' })
      return false
    }
    return true
  },

  validateForPublish() {
    if (!this.validateDraftContent()) return false
    if (this.data.selectedTags.length < 1) {
      wx.showToast({ title: '请至少选择 1 个标签', icon: 'none' })
      return false
    }
    return true
  },

  onBack() {
    if (!this.hasContent()) {
      this.navigateBackToSource()
      return
    }
    wx.showModal({
      title: '是否保存草稿？',
      confirmText: '保存',
      cancelText: '不保存',
      success: (res) => {
        if (res.confirm) {
          this.saveDraftAndLeave()
        } else if (res.cancel) {
          this.navigateBackToSource()
        }
      },
    })
  },

  navigateBackToSource() {
    const tabUrl = TAB_FROM_PAGES[this.data.from]
    if (tabUrl) {
      wx.switchTab({ url: tabUrl })
      return
    }
    const pages = getCurrentPages()
    if (pages.length > 1) {
      wx.navigateBack()
      return
    }
    wx.switchTab({ url: TAB_FROM_PAGES.home })
  },

  navigateAfterPublish() {
    const tabUrl = TAB_FROM_PAGES[this.data.from]
    if (tabUrl) {
      wx.switchTab({ url: tabUrl })
      return
    }
    const pages = getCurrentPages()
    if (pages.length > 1) {
      wx.navigateBack()
      return
    }
    wx.switchTab({ url: TAB_FROM_PAGES.mine })
  },

  async saveDraft(silent = false) {
    if (!this.validateDraftContent()) return false
    if (this.data.saving) return false
    this.setData({ saving: true })
    try {
      const body = this.buildDraftBody()
      if (this.data.articleId > 0) {
        await articleApi.updateDraft({ id: this.data.articleId, createDraftRequest: body })
      } else {
        const res = await articleApi.createDraft({ createDraftRequest: body })
        this.setData({ articleId: res.id || 0 })
      }
      if (!silent) {
        wx.showToast({ title: '草稿已保存', icon: 'success' })
      }
      return true
    } catch (e) {
      showApiError(e, '保存失败')
      return false
    } finally {
      this.setData({ saving: false })
    }
  },

  async saveDraftAndLeave() {
    const ok = await this.saveDraft(true)
    if (ok) {
      this.navigateBackToSource()
    }
  },

  async onSaveDraft() {
    await this.saveDraft(false)
  },

  async onPublish() {
    if (!this.validateForPublish()) return
    this.setData({ publishing: true })
    try {
      let articleId = this.data.articleId
      const body = this.buildDraftBody()
      if (articleId > 0) {
        await articleApi.updateDraft({ id: articleId, createDraftRequest: body })
      } else {
        const created = await articleApi.createDraft({ createDraftRequest: body })
        articleId = created.id || 0
        this.setData({ articleId })
      }
      const res = await articleApi.publish({ id: articleId })
      this.setData({ showSettings: false })
      wx.showToast({ title: res.message || '已提交', icon: 'none' })
      this.navigateAfterPublish()
    } catch (e) {
      showApiError(e, '发布失败')
    } finally {
      this.setData({ publishing: false })
    }
  },
})
