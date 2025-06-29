import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router' // We'll create this next

import './assets/main.css' // Basic styling (optional, can be created later)

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
