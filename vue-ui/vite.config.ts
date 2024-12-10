import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath } from 'url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
          @use '@/styles/_variables' as *;
          @use '@/styles/_mixins' as *;
        `
      }
    }
  },
  optimizeDeps: {
    include: [
      'element-plus',
      'fuse.js',
      'screenfull',
      'path-browserify'
    ]
  },
  server: {
    port: 3000,
    host: true,
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        ws: true,
        rewrite: (path) => path
      }
    }
  }
}) 