<template>
  <div class="bookmark-card">
    <h4 class="bookmark-title">
      <a :href="bookmark.url" target="_blank" rel="noopener noreferrer">{{ bookmark.title || 'Untitled Bookmark' }}</a>
    </h4>
    <p v-if="bookmark.description" class="bookmark-description">{{ bookmark.description }}</p>
    <p class="bookmark-url">
      <a :href="bookmark.url" target="_blank" rel="noopener noreferrer">{{ bookmark.url }}</a>
    </p>
    <div v-if="bookmark.tags && bookmark.tags.length" class="bookmark-tags">
      <span v-for="tag in bookmark.tags" :key="tag" class="tag" @click="emitTagClick(tag)">
        {{ tag }}
      </span>
    </div>
    <div class="bookmark-actions">
      <button @click="emitEdit" class="action-button edit-button">Edit</button>
      <button @click="emitDelete" class="action-button delete-button">Delete</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  bookmark: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['edit', 'delete', 'tagClicked']);

const emitEdit = () => {
  emit('edit', props.bookmark);
};

const emitDelete = () => {
  emit('delete', props.bookmark.id);
};

const emitTagClick = (tag) => {
  emit('tagClicked', tag);
};
</script>

<style scoped>
.bookmark-card {
  background-color: var(--white);
  border: 1px solid var(--medium-gray);
  border-radius: var(--border-radius);
  padding: 1rem; /* 16px */
  margin-bottom: 1rem; /* Consistent spacing */
  box-shadow: var(--box-shadow);
  transition: box-shadow 0.2s ease-in-out, border-color 0.2s ease-in-out;
  display: flex;
  flex-direction: column;
}

.bookmark-card:hover {
  box-shadow: 0 0.25rem 0.5rem rgba(0,0,0,0.1);
  border-color: var(--dark-gray);
}

.bookmark-title {
  margin-bottom: 0.5rem;
}

.bookmark-title a {
  color: var(--text-color);
  font-weight: 600; /* Slightly bolder for title */
  font-size: 1.15rem;
  /* text-decoration: none; from global */
}
.bookmark-title a:hover {
  /* text-decoration: underline; from global */
  color: var(--primary-color);
}

.bookmark-description {
  font-size: 0.9rem;
  color: var(--text-color-muted);
  margin-top: 0.25rem;
  margin-bottom: 0.75rem;
  white-space: pre-wrap;
  flex-grow: 1; /* Allows description to take available space */
}

.bookmark-url {
  margin-bottom: 0.75rem;
}
.bookmark-url a {
  font-size: 0.875rem;
  /* color: var(--primary-color); from global */
  word-break: break-all;
}

.bookmark-tags {
  margin-top: auto; /* Pushes tags and actions to the bottom if description is short */
  margin-bottom: 0.75rem;
}

.tag {
  display: inline-block;
  background-color: var(--medium-gray);
  color: var(--text-color-muted);
  padding: 0.25rem 0.6rem; /* 4px 10px */
  border-radius: var(--border-radius);
  font-size: 0.8rem;
  margin-right: 0.375rem; /* 6px */
  margin-bottom: 0.375rem;
  cursor: pointer;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.tag:hover {
  background-color: var(--primary-color);
  color: var(--white);
}

.bookmark-actions {
  margin-top: 0.5rem; /* Reduced margin as tags provide some space */
  text-align: right;
  border-top: 1px solid var(--light-gray); /* Subtle separator */
  padding-top: 0.75rem;
}

.action-button {
  /* padding: 0.375rem 0.75rem; /* 6px 12px */ /* Adjusted in global button style */
  /* font-size: 0.875rem; */ /* Adjusted in global button style */
  margin-left: 0.5rem; /* 8px */
}

.edit-button {
  background-color: var(--warning-color);
  color: var(--text-color); /* Better contrast for yellow */
  border-color: var(--warning-color);
}
.edit-button:hover {
  background-color: #e0a800; /* Darker yellow */
  border-color: #e0a800;
}

.delete-button {
  background-color: var(--danger-color);
  color: var(--white);
  border-color: var(--danger-color);
}
.delete-button:hover {
  background-color: #c82333; /* Darker red */
  border-color: #c82333;
}
</style>
