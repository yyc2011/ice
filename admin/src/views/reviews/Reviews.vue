<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminReviewApi } from '../../api/instances'
import type { ReviewDetailDto, ReviewListItemDto } from '../../api/generated'

const items = ref<ReviewListItemDto[]>([])
const selectedId = ref<number | null>(null)
const detail = ref<ReviewDetailDto | null>(null)
const loading = ref(false)
const rejectDialog = ref(false)
const reasonCode = ref('quality_low')
const reasonText = ref('')

const reasonOptions = [
  { value: 'quality_low', label: '内容质量不足' },
  { value: 'policy_violation', label: '违反社区规范' },
  { value: 'spam', label: '垃圾/广告内容' },
  { value: 'other', label: '其他' },
]

async function loadList() {
  loading.value = true
  try {
    const res = await adminReviewApi.list4()
    items.value = res.items || []
  } finally {
    loading.value = false
  }
}

async function openDetail(row: ReviewListItemDto) {
  if (!row.id) return
  selectedId.value = row.id
  detail.value = await adminReviewApi.get({ id: row.id })
}

async function onApprove() {
  if (!selectedId.value) return
  await adminReviewApi.approve({ id: selectedId.value })
  await loadList()
  if (selectedId.value) {
    detail.value = await adminReviewApi.get({ id: selectedId.value })
  }
}

function openReject() {
  rejectDialog.value = true
}

async function onReject() {
  if (!selectedId.value) return
  await adminReviewApi.reject({
    id: selectedId.value,
    reviewRejectRequest: {
      reasonCode: reasonCode.value,
      reasonText: reasonText.value,
    },
  })
  rejectDialog.value = false
  await loadList()
  detail.value = await adminReviewApi.get({ id: selectedId.value })
}

onMounted(loadList)
</script>

<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :span="14">
        <el-table :data="items" v-loading="loading" @row-click="openDetail">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="reviewType" label="类型" width="90" />
          <el-table-column prop="priority" label="优先级" width="90" />
          <el-table-column label="创建时间" width="180">
            <template #default="{ row }">
              {{ row.createdAt ? row.createdAt.toLocaleString('zh-CN', { hour12: false }) : '' }}
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="10">
        <el-card v-if="detail">
          <template #header>审核详情 #{{ detail.id }}</template>
          <h3>{{ detail.title }}</h3>
          <p class="content">{{ detail.content }}</p>
          <p v-if="detail.appealText"><strong>申诉说明：</strong>{{ detail.appealText }}</p>
          <p v-if="detail.aiScore !== null && detail.aiScore !== undefined">AI 评分：{{ detail.aiScore }}</p>
          <p v-if="detail.aiDimensions">AI 维度：{{ detail.aiDimensions }}</p>
          <div class="actions">
            <el-button type="success" @click="onApprove">通过</el-button>
            <el-button type="danger" @click="openReject">拒绝</el-button>
          </div>
        </el-card>
        <el-empty v-else description="选择一条审核记录查看详情" />
      </el-col>
    </el-row>

    <el-dialog v-model="rejectDialog" title="拒绝审核" width="480px">
      <el-form>
        <el-form-item label="原因">
          <el-select v-model="reasonCode" style="width: 100%">
            <el-option v-for="opt in reasonOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="补充说明">
          <el-input v-model="reasonText" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialog = false">取消</el-button>
        <el-button type="danger" @click="onReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.content { white-space: pre-wrap; line-height: 1.7; margin: 12px 0; }
.actions { display: flex; gap: 12px; margin-top: 16px; }
</style>
