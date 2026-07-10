<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const login_name = ref('admin')
const password = ref('admin123')
const loading = ref(false)
const error = ref('')
const isDev = import.meta.env.DEV

async function onSubmit() {
  loading.value = true
  error.value = ''
  try {
    await auth.login(login_name.value, password.value)
    router.push('/admin/reviews')
  } catch (e) {
    error.value = e instanceof Error ? e.message : '登录失败'
  } finally {
    loading.value = false
  }
}

async function onDevLogin() {
  loading.value = true
  error.value = ''
  try {
    await auth.devQuickLogin()
    router.push('/admin/reviews')
  } catch (e) {
    error.value = e instanceof Error ? e.message : '开发登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="card">
      <h2>管理后台登录</h2>
      <el-form @submit.prevent="onSubmit">
        <el-form-item label="用户名">
          <el-input v-model="login_name" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password" type="password" show-password />
        </el-form-item>
        <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" class="mb" />
        <el-button type="primary" native-type="submit" :loading="loading" block>登录</el-button>
        <el-button v-if="isDev" class="mt" block @click="onDevLogin">开发环境一键登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.card { width: 400px; }
.mb { margin-bottom: 12px; }
.mt { margin-top: 12px; }
</style>
