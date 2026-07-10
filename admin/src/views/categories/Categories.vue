<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminCategoryApi } from '../../api/instances'
import type { AdminCategoryDto } from '../../api/generated'

const items = ref<AdminCategoryDto[]>([])
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const form = ref({
  parentId: 0,
  name: '',
  sortOrder: 0,
  isHomeRecommended: false,
  homeSortOrder: 0,
})

async function load() {
  items.value = await adminCategoryApi.list2()
}

function openCreate() {
  editingId.value = null
  form.value = { parentId: 0, name: '', sortOrder: 0, isHomeRecommended: false, homeSortOrder: 0 }
  dialogVisible.value = true
}

function openEdit(row: AdminCategoryDto) {
  editingId.value = row.id || null
  form.value = {
    parentId: row.parentId || 0,
    name: row.name || '',
    sortOrder: 0,
    isHomeRecommended: row.isHomeRecommended || false,
    homeSortOrder: row.homeSortOrder || 0,
  }
  dialogVisible.value = true
}

async function onSubmit() {
  if (editingId.value) {
    await adminCategoryApi.update({
      id: editingId.value,
      adminCategoryRequest: form.value,
    })
  } else {
    await adminCategoryApi.create2({ adminCategoryRequest: form.value })
  }
  dialogVisible.value = false
  await load()
}

async function onDelete(row: AdminCategoryDto) {
  if (!row.id) return
  await adminCategoryApi._delete({ id: row.id })
  await load()
}

onMounted(load)
</script>

<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="openCreate">新建分类</el-button>
    </div>
    <el-table :data="items">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="parentId" label="父级 ID" width="100" />
      <el-table-column label="首页推荐" width="100">
        <template #default="{ row }">{{ row.isHomeRecommended ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="homeSortOrder" label="首页排序" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm title="确认删除？" @confirm="onDelete(row)">
            <template #reference>
              <el-button link type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑分类' : '新建分类'" width="480px">
      <el-form label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="父级 ID"><el-input-number v-model="form.parentId" :min="0" /></el-form-item>
        <el-form-item label="首页推荐"><el-switch v-model="form.isHomeRecommended" /></el-form-item>
        <el-form-item label="首页排序"><el-input-number v-model="form.homeSortOrder" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar { margin-bottom: 16px; }
</style>
