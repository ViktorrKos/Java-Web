import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  base: './',
  plugins: [vue()],
  build: {
    outDir: 'dist',
    emptyOutDir: true
  },
  server: {
    proxy: {
      '/pot': {
        target: 'http://localhost:9090/WebLab-1.0-SNAPSHOT/',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
