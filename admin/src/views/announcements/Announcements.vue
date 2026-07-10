<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminAnnouncementApi } from '../../api/instances'
import type { AnnouncementDto } from '../../api/generated'

const items = ref<AnnouncementDto[]>([])
const title = ref('')
const content = ref('')
const publishNow = ref(true)

async function load() {
  items.value = await adminAnnouncementApi.list3()
}

async function onCreate() {
  await adminAnnouncementApi.create3({
    createAnnouncementRequest: {
      title: title.value,
      content: content.value,
      publishNow: publishNow.value,
    },
  })
  title.value = ''
  content.value = ''
  await load()
}

onMounted(load)
</script>

<template>
  <el-row :gutter="16">
    <el-col :span="10">
      <el-card header="发布公告">
        <el-form label-width="80px">
          <el-form-item label="标题"><el-input v-model="title" /></el-form-item>
          <el-form-item label="内容"><el-input v-model="content" type="textarea" :rows="6" /></el-form-item>
          <el-form-item label="立即发布"><el-switch v-model="publishNow" /></el-form-item>
          <el-button type="primary" @click="onCreate">提交</el-button>
        </el-form>
      </el-card>
    </el-col>
    <el-col :span="14">
      <el-table :data="items">
        <el-table-column prop="title" label="标题" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">{{ row.published ? '已发布' : '草稿' }}</template>
        </el-table-column>
        <el-table-column prop="publishedAt" label="发布时间" width="180" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
      </el-table>
    </el-col>
  </el-row>
</template>
