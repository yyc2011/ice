<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminReportApi } from '../../api/instances'
import type { ReportDto } from '../../api/generated'

const items = ref<ReportDto[]>([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    items.value = await adminReportApi.list5()
  } finally {
    loading.value = false
  }
}

async function onResolve(row: ReportDto, accepted: boolean) {
  if (!row.id) return
  await adminReportApi.resolve({ id: row.id, reportResolveRequest: { accepted } })
  await load()
}

onMounted(load)
</script>

<template>
  <el-table :data="items" v-loading="loading">
    <el-table-column prop="id" label="ID" width="80" />
    <el-table-column prop="reporterId" label="举报人" width="100" />
    <el-table-column prop="targetType" label="类型" width="80" />
    <el-table-column prop="targetId" label="目标 ID" width="100" />
    <el-table-column prop="reason" label="原因" />
    <el-table-column prop="reasonDetail" label="详情" />
    <el-table-column prop="createdAt" label="时间" width="180" />
    <el-table-column label="操作" width="200">
      <template #default="{ row }">
        <el-button type="success" link @click="onResolve(row, true)">成立</el-button>
        <el-button type="info" link @click="onResolve(row, false)">不成立</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
