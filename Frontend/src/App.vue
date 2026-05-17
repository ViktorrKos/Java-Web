<script setup>
import { onMounted, ref } from 'vue'

const API_ENDPOINT = '/pot'
const TEXT = {
  eyebrow: 'Lab 3 CRUD',
  title: 'Каталог цифрових камер',
  description: 'Додавання, редагування та видалення камер через сервлет /pot.',
  loadError: 'Не вдалося отримати дані з сервера. Перевірте backend і шлях /pot.',
  saveError: 'Не вдалося зберегти запис. Перевірте відповідь сервера.',
  deleteError: 'Не вдалося видалити запис. Перевірте, чи backend приймає id.',
  retry: 'Спробувати ще раз',
  empty: 'Список камер порожній.',
  add: 'Додати камеру',
  update: 'Зберегти зміни',
  cancel: 'Скасувати',
  edit: 'Редагувати',
  delete: 'Видалити',
  brand: 'Бренд',
  model: 'Модель',
  megapixels: 'Мегапікселі (MP)',
  price: 'Ціна ($)',
  descriptionLabel: 'Опис',
  actions: 'Дії'
}

const cameras = ref([])
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const formError = ref('')
const editingId = ref(null)
const form = ref(createEmptyForm())

function createEmptyForm() {
  return {
    brand: '',
    model: '',
    megapixels: '',
    price: '',
    description: ''
  }
}

function toNumber(value) {
  const normalized = String(value).replace(',', '.')
  const number = Number(normalized)
  return Number.isFinite(number) ? number : 0
}

function normalizeCamera(cam) {
  return {
    id: cam.id,
    brand: cam.brand ?? '',
    model: cam.model ?? '',
    megapixels: cam.megapixels ?? '',
    price: cam.price ?? '',
    description: cam.description ?? ''
  }
}

function buildPayload() {
  return {
    ...(editingId.value !== null ? { id: editingId.value } : {}),
    brand: form.value.brand.trim(),
    model: form.value.model.trim(),
    megapixels: parseInt(form.value.megapixels) || 0,
    price: toNumber(form.value.price),
    description: form.value.description.trim()
  }
}

function resetForm() {
  editingId.value = null
  form.value = createEmptyForm()
  formError.value = ''
}

function editCamera(cam) {
  const normalized = normalizeCamera(cam)
  editingId.value = normalized.id
  form.value = {
    brand: normalized.brand,
    model: normalized.model,
    megapixels: normalized.megapixels,
    price: normalized.price,
    description: normalized.description
  }
  formError.value = ''
}

function getCameraUrl(id) {
  return `${API_ENDPOINT}?id=${encodeURIComponent(id)}`
}

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {})
    },
    ...options
  })

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`)
  }

  return response
}

async function loadCameras() {
  loading.value = true
  error.value = ''

  try {
    const response = await fetch(API_ENDPOINT)

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const data = await response.json()
    cameras.value = Array.isArray(data) ? data.map(normalizeCamera) : []
  } catch (err) {
    error.value = TEXT.loadError
    console.error('Camera loading failed:', err)
  } finally {
    loading.value = false
  }
}

async function saveCamera() {
  formError.value = ''

  if (!form.value.brand.trim() || !form.value.model.trim()) {
    formError.value = 'Заповніть бренд та модель.'
    return
  }

  saving.value = true

  try {
    const payload = buildPayload()
    const isEditing = editingId.value !== null

    await requestJson(isEditing ? getCameraUrl(editingId.value) : API_ENDPOINT, {
      method: isEditing ? 'PUT' : 'POST',
      body: JSON.stringify(payload)
    })

    resetForm()
    await loadCameras()
  } catch (err) {
    formError.value = TEXT.saveError
    console.error('Camera saving failed:', err)
  } finally {
    saving.value = false
  }
}

async function deleteCamera(cam) {
  const id = cam.id

  if (id === undefined || id === null) {
    error.value = TEXT.deleteError
    return
  }

  try {
    await requestJson(getCameraUrl(id), { method: 'DELETE' })

    if (editingId.value === id) {
      resetForm()
    }

    await loadCameras()
  } catch (err) {
    error.value = TEXT.deleteError
    console.error('Camera deleting failed:', err)
  }
}

onMounted(loadCameras)
</script>

<template>
  <main class="app">
    <section class="page-header">
      <p class="eyebrow">{{ TEXT.eyebrow }}</p>
      <h1>{{ TEXT.title }}</h1>
      <p class="description">{{ TEXT.description }}</p>
    </section>

    <section class="workspace">
      <form class="pot-form" @submit.prevent="saveCamera">
        <div class="form-header">
          <h2>{{ editingId === null ? TEXT.add : TEXT.update }}</h2>
          <button v-if="editingId !== null" type="button" class="button-muted" @click="resetForm">
            {{ TEXT.cancel }}
          </button>
        </div>

        <label>
          <span>{{ TEXT.brand }}</span>
          <input v-model="form.brand" type="text" name="brand" required placeholder="Напр. Sony, Canon" />
        </label>

        <label>
          <span>{{ TEXT.model }}</span>
          <input v-model="form.model" type="text" name="model" required placeholder="Напр. Alpha 7 IV" />
        </label>

        <div class="form-grid">
          <label>
            <span>{{ TEXT.megapixels }}</span>
            <input v-model="form.megapixels" type="number" name="megapixels" min="0" placeholder="33" />
          </label>

          <label>
            <span>{{ TEXT.price }}</span>
            <input v-model="form.price" type="number" name="price" min="0" step="0.01" placeholder="2499.99" />
          </label>
        </div>

        <label>
          <span>{{ TEXT.descriptionLabel }}</span>
          <textarea v-model="form.description" name="description" rows="3" placeholder="Короткий опис камери..."></textarea>
        </label>

        <p v-if="formError" class="inline-error">{{ formError }}</p>

        <button type="submit" :disabled="saving">
          {{ editingId === null ? TEXT.add : TEXT.update }}
        </button>
      </form>

      <section class="content" aria-live="polite">
        <div v-if="loading" class="status">Завантаження...</div>

        <div v-else-if="error" class="message message-error">
          <p>{{ error }}</p>
          <button type="button" @click="loadCameras">{{ TEXT.retry }}</button>
        </div>

        <div v-else-if="cameras.length === 0" class="message">
          <p>{{ TEXT.empty }}</p>
        </div>

        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>{{ TEXT.brand }}</th>
                <th>{{ TEXT.model }}</th>
                <th>{{ TEXT.megapixels }}</th>
                <th>{{ TEXT.price }}</th>
                <th>{{ TEXT.descriptionLabel }}</th>
                <th>{{ TEXT.actions }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cam in cameras" :key="cam.id">
                <td class="muted">#{{ cam.id }}</td>
                <td><strong>{{ cam.brand }}</strong></td>
                <td>{{ cam.model }}</td>
                <td>{{ cam.megapixels }} MP</td>
                <td class="price-cell">${{ cam.price }}</td>
                <td class="desc-cell">{{ cam.description }}</td>
                <td>
                  <div class="row-actions">
                    <button type="button" class="button-muted" @click="editCamera(cam)">
                      {{ TEXT.edit }}
                    </button>
                    <button type="button" class="button-danger" @click="deleteCamera(cam)">
                      {{ TEXT.delete }}
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </section>
  </main>
</template>

<style scoped>
.price-cell {
  font-weight: 600;
  color: #10b981;
}
.desc-cell {
  max-width: 250px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
