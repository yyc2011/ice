/* 轻快冷色调渐变（无封面时使用） */
const COOL_PALETTE = [
  'linear-gradient(145deg, #c2f0f7 0%, #a8e6f0 100%)',
  'linear-gradient(145deg, #d4f5e9 0%, #b8ebd6 100%)',
  'linear-gradient(145deg, #dce8ff 0%, #c2d4ff 100%)',
  'linear-gradient(145deg, #e0f7f4 0%, #b2ebe3 100%)',
  'linear-gradient(145deg, #e8f4ff 0%, #cfe4ff 100%)',
  'linear-gradient(145deg, #d9f2ec 0%, #b5e4d8 100%)',
  'linear-gradient(145deg, #e4f0ff 0%, #d0e2ff 100%)',
  'linear-gradient(145deg, #d6f3f0 0%, #bce8e2 100%)',
]

function pickCoolBg(id?: number): string {
  const n = typeof id === 'number' && id > 0 ? id : 0
  return COOL_PALETTE[n % COOL_PALETTE.length]
}

Component({
  properties: {
    item: {
      type: Object,
      value: {},
    },
    showJoin: {
      type: Boolean,
      value: true,
    },
  },
  data: {
    bgStyle: COOL_PALETTE[0],
  },
  observers: {
    item(item: { id?: number }) {
      this.setData({ bgStyle: pickCoolBg(item?.id) })
    },
  },
  methods: {
    onTapCard() {
      const id = (this.data.item as { id?: number }).id
      if (id) {
        this.triggerEvent('tapcard', { id })
      }
    },
    onTapJoin() {
      const item = this.data.item as { id?: number; title?: string }
      if (item.id) {
        this.triggerEvent('tapjoin', { id: item.id, title: item.title || '' })
      }
    },
  },
})
