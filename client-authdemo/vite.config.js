import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'
import generateViews from "./plugins/vite-plugin-generate-views.js"

// https://vite.dev/config/
export default defineConfig({
  plugins: [svelte(), generateViews()],
})
