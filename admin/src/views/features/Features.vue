<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminFeatureApi } from '../../api/instances'
import type { FeatureConfigDto } from '../../api/generated'

const items = ref<FeatureConfigDto[]>([])

async function load() {
  items.value = await adminFeatureApi.list1()
}

async function onToggle(row: FeatureConfigDto) {
  const next = row.value === 'true' ? 'false' : 'true'
  await adminFeatureApi.update1({
    featureUpdateRequest: {
      items: [{ key: row.key, value: next }],
    },
  })
  await load()
}

onMounted(load)
</script>

<template>
  <el-table :data="items">
    <el-table-column prop="key" label="配置项" />
    <el-table-column label="状态" width="160">
      <template #default="{ row }">
        <el-switch
          :model-value="row.value === 'true'"
          @change="onToggle(row)"
        />
      </template>
    </el-table-column>
    <el-table-column prop="value" label="当前值" width="120" />
  </el-table>
</template>
