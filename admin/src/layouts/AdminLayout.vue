<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const menus = [
  { path: '/admin/reviews', label: '审核队列' },
  { path: '/admin/categories', label: '分类管理' },
  { path: '/admin/reports', label: '举报处理' },
  { path: '/admin/announcements', label: '公告发布' },
  { path: '/admin/features', label: '功能开关' },
]

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="brand">ICE 管理后台</div>
      <el-menu :default-active="route.path" router>
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          {{ item.label }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>{{ auth.nickname || '管理员' }}</span>
        <el-button link type="danger" @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout { min-height: 100vh; }
.aside { border-right: 1px solid #eee; background: #fff; }
.brand { padding: 20px 16px; font-weight: 700; }
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  background: #fff;
}
</style>
