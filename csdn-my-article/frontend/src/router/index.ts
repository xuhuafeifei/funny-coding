import { createRouter, createWebHistory } from 'vue-router'
import MarkdownViewer from '../components/MarkdownViewer.vue'
import HelloWorld from '../components/HelloWorld.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'markdown',
      component: MarkdownViewer
    },
    {
      path: '/hello',
      name: 'hello',
      component: HelloWorld
    }
  ]
})

export default router