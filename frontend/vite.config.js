import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: { // Optional: configure dev server
    port: 5173, // Default is 5173
    proxy: {
      // Proxy API requests to Spring Boot backend
      '/api': {
        target: 'http://localhost:8080', // Assuming Spring Boot runs on 8080
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '') // if backend doesn't have /api prefix
      }
    }
  }
})
