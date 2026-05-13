import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      // Коли фронтенд звертається до /api, Vite перенаправляє це на Tomcat
      '/api': {
        target: 'http://localhost:9090/WebLab-1.0-SNAPSHOT/',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})