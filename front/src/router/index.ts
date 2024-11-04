import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WriteView from '@/views/WriteView.vue'
import ReadView from '@/views/ReadView.vue'
import LoginView from '@/views/LoginView.vue'
import EditView from '@/views/EditView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/write',
      name: 'write',
      component: WriteView
    },
    {
      path: '/edit/:postId',
      name: 'edit',
      component: EditView,
      props: true
    },
    {
      path: '/post/:postId',
      name: 'post',
      component: ReadView,
      props: true
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // }
  ]
})

export default router
