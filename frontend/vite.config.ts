import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server : {
    port : 3000
  },
  base: '/' // 루트 디렉토리 명시적 설정
})
