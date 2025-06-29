<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h3>Edit Bookmark</h3>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="edit-url">URL (Required):</label>
          <input type="url" id="edit-url" v.model.trim="editableBookmark.url" required />
          <div v-if="formErrors.url" class="error-message">{{ formErrors.url }}</div>
        </div>
        <div class="form-group">
          <label for="edit-title">Title:</label>
          <input type="text" id="edit-title" v.model.trim="editableBookmark.title" />
        </div>
        <div class="form-group">
          <label for="edit-description">Description:</label>
          <textarea id="edit-description" v.model.trim="editableBookmark.description"></textarea>
        </div>
        <div class="form-group">
          <label for="edit-tags">Tags (comma-separated):</label>
          <input type="text" id="edit-tags" v.model.trim="tagsStr" @input="parseTags" />
        </div>
        <div class="modal-actions">
          <button type="submit" :disabled="bookmarksStore.isLoading">
            {{ bookmarksStore.isLoading ? 'Saving...' : 'Save Changes' }}
          </button>
          <button type="button" @click="closeModal" class="cancel-button">Cancel</button>
        </div>
        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
        <p v-if="bookmarksStore.error && !formErrors.submit" class="error-message">
          Error: {{ bookmarksStore.error }}
        </p>
         <p v-if="formErrors.submit" class="error-message">
          {{ formErrors.submit }}
        </p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, defineProps, defineEmits } from 'vue';
import { useBookmarksStore } from '@/store/bookmarks';

const props = defineProps({
  isVisible: Boolean,
  bookmarkToEdit: Object
});

const emit = defineEmits(['close', 'bookmark-updated']);

const bookmarksStore = useBookmarksStore();
const editableBookmark = reactive({ id: null, url: '', title: '', description: '', tags: [] });
const tagsStr = ref(''); // For user input of tags

const formErrors = reactive({
  url: '',
  submit: ''
});
const successMessage = ref('');

// Watch for changes in bookmarkToEdit prop to populate the form
watch(() => props.bookmarkToEdit, (newVal) => {
  if (newVal) {
    Object.assign(editableBookmark, JSON.parse(JSON.stringify(newVal))); // Deep copy
    tagsStr.value = newVal.tags ? newVal.tags.join(', ') : '';
    // Clear previous messages
    successMessage.value = '';
    formErrors.url = '';
    formErrors.submit = '';
    bookmarksStore.error = null;
  }
}, { immediate: true, deep: true });


const validateUrl = (url) => {
  if (!url) return "URL is required.";
  try {
    new URL(url);
    if (!url.startsWith('http://') && !url.startsWith('https://')) {
        return "URL must start with http:// or https://";
    }
  } catch (_) {
    return "Please enter a valid URL (e.g., http://example.com).";
  }
  return '';
};

const parseTags = () => {
  editableBookmark.tags = tagsStr.value.split(',').map(tag => tag.trim()).filter(tag => tag !== '');
};

const handleSubmit = async () => {
  successMessage.value = '';
  formErrors.url = '';
  formErrors.submit = '';
  bookmarksStore.error = null;

  formErrors.url = validateUrl(editableBookmark.url);
  if (formErrors.url) {
    return;
  }

  parseTags(); // Ensure tags are parsed before submission

  try {
    await bookmarksStore.updateBookmark(editableBookmark.id, { ...editableBookmark });
    if (!bookmarksStore.error) {
      successMessage.value = 'Bookmark updated successfully!';
      emit('bookmark-updated');
      // Optionally close modal after a short delay to show success message
      setTimeout(closeModal, 1500);
    }
  } catch (error) {
    console.error('Update error in component:', error);
    formErrors.submit = 'An unexpected error occurred. Please try again.';
  }
};

const closeModal = () => {
  emit('close');
};

</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6); /* Darker overlay */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050; /* Higher z-index for modals */
  padding: 1rem; /* Add padding for small screens so modal doesn't touch edges */
}

.modal-content {
  background-color: var(--white);
  padding: 20px 25px; /* Adjusted padding */
  border-radius: var(--border-radius);
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
  width: 100%; /* Full width on small screens */
  max-width: 500px;
  animation: fadeInModal 0.3s ease-out;
}

@keyframes fadeInModal {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  text-align: center;
  font-weight: 500;
  color: var(--text-color);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.375rem;
  font-weight: 500;
  color: var(--text-color-muted);
}

.form-group input[type="text"],
.form-group input[type="url"],
.form-group textarea {
  width: 100%;
  padding: 0.625rem;
  border: 1px solid var(--dark-gray);
  border-radius: var(--border-radius);
  box-sizing: border-box;
  font-size: 0.95rem;
  color: var(--text-color);
  background-color: var(--white);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.form-group input[type="text"]:focus,
.form-group input[type="url"]:focus,
.form-group textarea:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  outline: none;
}

.form-group textarea {
  min-height: 90px;
  resize: vertical;
}

.modal-actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem; /* 12px */
}

.modal-actions button {
  /* padding: 0.625rem 1.125rem; /* 10px 18px */
  /* font-size: 0.95rem; Adjusted via global button style */
  font-weight: 500;
}

.modal-actions button[type="submit"] {
  background-color: var(--primary-color);
  color: var(--white);
}
.modal-actions button[type="submit"]:hover {
  background-color: var(--primary-color-dark);
}
.modal-actions button[type="submit"]:disabled {
  background-color: var(--medium-gray);
  color: var(--text-color-muted);
}

.cancel-button {
  background-color: var(--secondary-color);
  color: var(--white);
  border: 1px solid var(--secondary-color);
}
.cancel-button:hover {
  background-color: var(--secondary-color-dark);
  border-color: var(--secondary-color-dark);
}

/* .error-message and .success-message are now global */
.success-message {
  text-align: center; /* Ensure it's centered */
}
</style>
