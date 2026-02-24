import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { visualizer } from 'rollup-plugin-visualizer'


export default defineConfig({
  plugins: [
	react(),
	tailwindcss(),
	visualizer({
	  filename: './dist/stats.html',
	  open: false,
	  gzipSize: true,
	  brotliSize: true,
	})
 ],
 build: {
   rollupOptions: {
     output: {
       manualChunks: {
         'vendor-react': ['react', 'react-dom', 'react-router-dom'],
         'vendor-antd': ['antd', '@ant-design/icons'],
         'vendor-redux': ['@reduxjs/toolkit', 'react-redux'],
       },
     },
   },
   chunkSizeWarningLimit: 600,
 },
 resolve: {
   alias: {
     'process': 'process/browser',
   },
 },
})
