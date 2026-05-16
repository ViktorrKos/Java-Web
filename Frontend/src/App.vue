<script setup>
import { onMounted, ref } from 'vue'

const API_ENDPOINT = '/pot'
const TEXT = {
  eyebrow: 'Lab 3 CRUD',
  title: 'Каталог каструль',
  description: 'Додавання, редагування та видалення записів через сервлет /pot.',
  loadError: 'Не вдалося отримати дані з сервера. Перевірте backend і шлях /pot.',
  saveError: 'Не вдалося зберегти запис. Перевірте відповідь сервера.',
  deleteError: 'Не вдалося видалити запис. Перевірте, чи backend приймає id.',
  retry: 'Спробувати ще раз',
  empty: 'Список каструль порожній.',
  add: 'Додати',
  update: 'Зберегти',
  cancel: 'Скасувати',
  edit: 'Редагувати',
  delete: 'Видалити',
  name: 'Назва',
  material: 'Матеріал',
  volume: "Об'єм, л",
  price: 'Ціна',
  actions: 'Дії'
}

const pots = ref([])
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const formError = ref('')
const editingId = ref(null)
const form = ref(createEmptyForm())

function createEmptyForm() {
  return {
    name: '',
    material: '',
    volume: '',
    price: ''
  }
}

function toNumber(value) {
  const normalized = String(value).replace(',', '.')
  const number = Number(normalized)

  return Number.isFinite(number) ? number : 0
}

function normalizePot(pot) {
  return {
    id: pot.id,
    name: pot.name ?? pot.title ?? pot.model ?? '',
    material: pot.material ?? pot.brand ?? '',
    volume: pot.volume ?? pot.capacity ?? '',
    price: pot.price ?? ''
  }
}

function buildPayload() {
  return {
    ...(editingId.value !== null ? { id: editingId.value } : {}),
    name: form.value.name.trim(),
    material: form.value.material.trim(),
    volume: toNumber(form.value.volume),
    price: toNumber(form.value.price)
  }
}

function resetForm() {
  editingId.value = null
  form.value = createEmptyForm()
  formError.value = ''
}

function editPot(pot) {
  const normalized = normalizePot(pot)
  editingId.value = normalized.id
  form.value = {
    name: normalized.name,
    material: normalized.material,
    volume: normalized.volume,
    price: normalized.price
  }
  formError.value = ''
}

function getPotUrl(id) {
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

async function loadPots() {
  loading.value = true
  error.value = ''

  try {
    const response = await fetch(API_ENDPOINT)

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const data = await response.json()
    pots.value = Array.isArray(data) ? data.map(normalizePot) : []
  } catch (err) {
    error.value = TEXT.loadError
    console.error('Pot loading failed:', err)
  } finally {
    loading.value = false
  }
}

async function savePot() {
  formError.value = ''

  if (!form.value.name.trim() || !form.value.material.trim()) {
    formError.value = 'Заповніть назву та матеріал.'
    return
  }

  saving.value = true

  try {
    const payload = buildPayload()
    const isEditing = editingId.value !== null

    await requestJson(isEditing ? getPotUrl(editingId.value) : API_ENDPOINT, {
      method: isEditing ? 'PUT' : 'POST',
      body: JSON.stringify(payload)
    })

    resetForm()
    await loadPots()
  } catch (err) {
    formError.value = TEXT.saveError
    console.error('Pot saving failed:', err)
  } finally {
    saving.value = false
  }
}

async function deletePot(pot) {
  const id = pot.id

  if (id === undefined || id === null) {
    error.value = TEXT.deleteError
    return
  }

  try {
    await requestJson(getPotUrl(id), { method: 'DELETE' })

    if (editingId.value === id) {
      resetForm()
    }

    await loadPots()
  } catch (err) {
    error.value = TEXT.deleteError
    console.error('Pot deleting failed:', err)
  }
}

onMounted(loadPots)
</script>

<template>
  <main class="app">
    <section class="page-header">
      <p class="eyebrow">{{ TEXT.eyebrow }}</p>
      <h1>{{ TEXT.title }}</h1>
      <p class="description">{{ TEXT.description }}</p>
    </section>

    <section class="workspace">
      <form class="pot-form" @submit.prevent="savePot">
        <div class="form-header">
          <h2>{{ editingId === null ? TEXT.add : TEXT.edit }}</h2>
          <button v-if="editingId !== null" type="button" class="button-muted" @click="resetForm">
            {{ TEXT.cancel }}
          </button>
        </div>

        <label>
          <span>{{ TEXT.name }}</span>
          <input v-model="form.name" type="text" name="name" required />
        </label>

        <label>
          <span>{{ TEXT.material }}</span>
          <input v-model="form.material" type="text" name="material" required />
        </label>

        <div class="form-grid">
          <label>
            <span>{{ TEXT.volume }}</span>
            <input v-model="form.volume" type="number" name="volume" min="0" step="0.1" />
          </label>

          <label>
            <span>{{ TEXT.price }}</span>
            <input v-model="form.price" type="number" name="price" min="0" step="0.01" />
          </label>
        </div>

        <p v-if="formError" class="inline-error">{{ formError }}</p>

        <button type="submit" :disabled="saving">
          {{ editingId === null ? TEXT.add : TEXT.update }}
        </button>
      </form>

      <section class="content" aria-live="polite">
        <div v-if="loading" class="status">Завантаження...</div>

        <div v-else-if="error" class="message message-error">
          <p>{{ error }}</p>
          <button type="button" @click="loadPots">{{ TEXT.retry }}</button>
        </div>

        <div v-else-if="pots.length === 0" class="message">
          <p>{{ TEXT.empty }}</p>
        </div>

        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>{{ TEXT.name }}</th>
                <th>{{ TEXT.material }}</th>
                <th>{{ TEXT.volume }}</th>
                <th>{{ TEXT.price }}</th>
                <th>{{ TEXT.actions }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="pot in pots" :key="pot.id">
                <td class="muted">#{{ pot.id }}</td>
                <td>{{ pot.name }}</td>
                <td>{{ pot.material }}</td>
                <td>{{ pot.volume }}</td>
                <td>{{ pot.price }}</td>
                <td>
                  <div class="row-actions">
                    <button type="button" class="button-muted" @click="editPot(pot)">
                      {{ TEXT.edit }}
                    </button>
                    <button type="button" class="button-danger" @click="deletePot(pot)">
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
