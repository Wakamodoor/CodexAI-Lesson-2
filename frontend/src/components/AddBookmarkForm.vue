<template>
  <div class="add-bookmark-form">
    <h3>Add New Bookmark</h3>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="url">URL (Required):</label>
        <input type="url" id="url" v.model.trim="formData.url" required />
        <div v-if="formErrors.url" class="error-message">{{ formErrors.url }}</div>
      </div>
      <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" id="title" v.model.trim="formData.title" />
      </div>
      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" v.model.trim="formData.description"></textarea>
      </div>
      <div class="form-group">
        <label for="tags">Tags (comma-separated):</label>
        <input type="text" id="tags" v.model.trim="formData.tagsStr" @input="parseTags" />
      </div>
      <button type="submit" :disabled="bookmarksStore.isLoading">
        {{ bookmarksStore.isLoading ? 'Adding...' : 'Add Bookmark' }}
      </button>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      <p v-if="bookmarksStore.error && !formErrors.submit" class="error-message">
        Error: {{ bookmarksStore.error }}
      </p>
      <p v-if="formErrors.submit" class="error-message">
        {{ formErrors.submit }}
      </p>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, defineEmits } from 'vue'; // Added defineEmits
import { useBookmarksStore } from '@/store/bookmarks';

const emit = defineEmits(['bookmark-added']); // Define the event

const bookmarksStore = useBookmarksStore();

const formData = reactive({
  url: '',
  title: '',
  description: '',
  tagsStr: '', // For user input
  tags: []      // Parsed array of tags
});

const formErrors = reactive({
  url: '',
  submit: ''
});

const successMessage = ref('');

const validateUrl = (url) => {
  if (!url) {
    return "URL is required.";
  }
  try {
    new URL(url); // Basic URL validation
    // More robust validation might be needed for specific URL schemes if required
    if (!url.startsWith('http://') && !url.startsWith('https://')) {
        return "URL must start with http:// or https://";
    }
  } catch (_) {
    return "Please enter a valid URL (e.g., http://example.com).";
  }
  return '';
};

const parseTags = () => {
  formData.tags = formData.tagsStr.split(',').map(tag => tag.trim()).filter(tag => tag !== '');
};

const handleSubmit = async () => {
  successMessage.value = '';
  formErrors.url = '';
  formErrors.submit = '';
  bookmarksStore.error = null; // Clear previous store errors

  formErrors.url = validateUrl(formData.url);
  if (formErrors.url) {
    return;
  }

  parseTags(); // Ensure tags are parsed before submission

  const newBookmark = {
    url: formData.url,
    title: formData.title,
    description: formData.description,
    tags: formData.tags
  };

  try {
    await bookmarksStore.addBookmark(newBookmark);
    if (!bookmarksStore.error) {
      successMessage.value = 'Bookmark added successfully!';
      // Reset form
      formData.url = '';
      formData.title = '';
      formData.description = '';
      formData.tagsStr = '';
      formData.tags = [];
      emit('bookmark-added'); // Emit event after successful addition
    } else {
        // Error is already set in the store, will be displayed by the template
        // Or, if specific form error handling is needed:
        // formErrors.submit = bookmarksStore.error;
    }
  } catch (error) {
    // This catch block might be redundant if the store action handles and stores the error
    console.error('Submission error in component:', error);
    formErrors.submit = 'An unexpected error occurred. Please try again.';
  }
};
</script>

<style scoped>
.add-bookmark-form {
  background-color: var(--light-gray);
  padding: 20px;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  margin-bottom: 2rem; /* Use consistent spacing */
}

.add-bookmark-form h3 {
  margin-bottom: 1.5rem; /* More space after heading */
  text-align: center;
  font-weight: 500;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.375rem; /* 6px */
  font-weight: 500; /* Medium weight for labels */
  color: var(--text-color-muted);
}

.form-group input[type="text"],
.form-group input[type="url"],
.form-group textarea {
  width: 100%;
  padding: 0.625rem; /* 10px */
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
  min-height: 90px; /* Slightly taller */
  resize: vertical;
}

button[type="submit"] {
  background-color: var(--primary-color);
  color: var(--white);
  padding: 0.625rem 1.25rem; /* 10px 20px */
  border: none; /* Already in global button style */
  /* border-radius: var(--border-radius); Already in global */
  /* cursor: pointer; Already in global */
  font-size: 1rem; /* Slightly larger */
  font-weight: 500;
}

button[type="submit"]:hover {
  background-color: var(--primary-color-dark);
}

button[type="submit"]:disabled {
  background-color: var(--medium-gray);
  color: var(--text-color-muted);
  cursor: not-allowed;
}

/* .error-message and .success-message are now global */

</style>
