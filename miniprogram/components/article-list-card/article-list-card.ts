Component({
  properties: {
    item: {
      type: Object,
      value: {},
    },
    /** hot | featured | search */
    variant: {
      type: String,
      value: 'featured',
    },
    showRank: {
      type: Boolean,
      value: false,
    },
  },
  methods: {
    onTapCard() {
      const id = (this.data.item as { id?: number }).id
      if (id) {
        this.triggerEvent('tapcard', { id })
      }
    },
    onTapAuthor() {
      this.triggerEvent('tapauthor')
    },
    formatCount(n: number | undefined) {
      const v = n || 0
      if (v >= 10000) return `${(v / 10000).toFixed(1)}w`
      if (v >= 1000) return `${(v / 1000).toFixed(1)}k`
      return String(v)
    },
    formatDate(iso: string | undefined) {
      if (!iso) return ''
      const d = new Date(iso)
      if (Number.isNaN(d.getTime())) return iso.slice(0, 10)
      const m = `${d.getMonth() + 1}`.padStart(2, '0')
      const day = `${d.getDate()}`.padStart(2, '0')
      return `${d.getFullYear()}-${m}-${day}`
    },
  },
})
