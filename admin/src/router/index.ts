import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import AdminLayout from '../layouts/AdminLayout.vue'
import Login from '../views/Login.vue'
import Reviews from '../views/reviews/Reviews.vue'
import Categories from '../views/categories/Categories.vue'
import Reports from '../views/reports/Reports.vue'
import Announcements from '../views/announcements/Announcements.vue'
import Features from '../views/features/Features.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/admin/reviews',
      children: [
        { path: 'admin/reviews', component: Reviews },
        { path: 'admin/categories', component: Categories },
        { path: 'admin/reports', component: Reports },
        { path: 'admin/announcements', component: Announcements },
        { path: 'admin/features', component: Features },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.path !== '/login' && !auth.isLoggedIn) {
    return '/login'
  }
  if (to.path === '/login' && auth.isLoggedIn) {
    return '/admin/reviews'
  }
})

export default router
