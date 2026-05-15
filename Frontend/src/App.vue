<script setup>
import { onMounted, ref } from 'vue'

const API_ENDPOINT = 'pot'
const TEXT = {
  eyebrow: '\u0406\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0456\u0439\u043d\u0430 \u0441\u0438\u0441\u0442\u0435\u043c\u0430',
  title: '\u041a\u0430\u0442\u0430\u043b\u043e\u0433 \u043a\u0430\u043c\u0435\u0440',
  error: '\u041d\u0435 \u0432\u0434\u0430\u043b\u043e\u0441\u044f \u043e\u0442\u0440\u0438\u043c\u0430\u0442\u0438 \u0434\u0430\u043d\u0456 \u0437 \u0441\u0435\u0440\u0432\u0435\u0440\u0430. \u041f\u0435\u0440\u0435\u0432\u0456\u0440\u0442\u0435, \u0447\u0438 \u0437\u0430\u043f\u0443\u0449\u0435\u043d\u0438\u0439 backend \u0456 \u0447\u0438 \u0441\u0435\u0440\u0432\u043b\u0435\u0442 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u0438\u0439 \u0437\u0430 \u0448\u043b\u044f\u0445\u043e\u043c /pot.',
  retry: '\u0421\u043f\u0440\u043e\u0431\u0443\u0432\u0430\u0442\u0438 \u0449\u0435 \u0440\u0430\u0437',
  empty: '\u0421\u043f\u0438\u0441\u043e\u043a \u043a\u0430\u043c\u0435\u0440 \u043f\u043e\u0440\u043e\u0436\u043d\u0456\u0439.',
  megapixels: '\u041c\u0435\u0433\u0430\u043f\u0456\u043a\u0441\u0435\u043b\u0456',
  price: '\u0426\u0456\u043d\u0430'
}
const cameras = ref([])
const loading = ref(true)
const error = ref('')

async function loadCameras() {
  loading.value = true
  error.value = ''

  try {
    const response = await fetch(API_ENDPOINT)

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const data = await response.json()
    cameras.value = Array.isArray(data) ? data : []
  } catch (err) {
    error.value = TEXT.error
    console.error('Camera loading failed:', err)
  } finally {
    loading.value = false
  }
}

onMounted(loadCameras)
</script>

<template>
  <main class="app">
    <section class="page-header">
      <p class="eyebrow">{{ TEXT.eyebrow }}</p>
      <h1>{{ TEXT.title }}</h1>
    </section>

    <section class="content" aria-live="polite">
      <template v-if="loading"></template>

      <div v-else-if="error" class="message message-error">
        <p>{{ error }}</p>
        <button type="button" @click="loadCameras">{{ TEXT.retry }}</button>
      </div>

      <div v-else-if="cameras.length === 0" class="message">
        <p>{{ TEXT.empty }}</p>
      </div>

      <div v-else class="camera-grid">
        <article v-for="camera in cameras" :key="camera.id" class="camera-card">
          <div class="camera-card__header">
            <span class="camera-id">#{{ camera.id }}</span>
            <strong>{{ camera.brand }}</strong>
          </div>
          <h2>{{ camera.model }}</h2>
          <dl>
            <div>
              <dt>{{ TEXT.megapixels }}</dt>
              <dd>{{ camera.megapixels }} MP</dd>
            </div>
            <div>
              <dt>{{ TEXT.price }}</dt>
              <dd>${{ camera.price }}</dd>
            </div>
          </dl>
        </article>
      </div>
    </section>
  </main>
</template>
