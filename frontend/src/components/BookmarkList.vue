<template>
  <div class="bookmark-list-container">
    <div v-if="bookmarksStore.isLoading" class="loading-message">Loading bookmarks...</div>
    <div v-if="bookmarksStore.error && !bookmarksStore.isLoading" class="error-message">
      Error loading bookmarks: {{ bookmarksStore.error }}
    </div>
    <div v-if="!bookmarksStore.isLoading && !bookmarksStore.error && bookmarks.length === 0" class="empty-message">
      No bookmarks found.
    </div>

    <div v-if="!bookmarksStore.isLoading && !bookmarksStore.error && bookmarks.length > 0" class="bookmarks-grid">
      <BookmarkItem
        v-for="bookmark in bookmarks"
        :key="bookmark.id"
        :bookmark="bookmark"
        @edit="handleEdit"
        @delete="handleDelete"
        @tagClicked="handleTagClick"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, defineEmits } from 'vue';
import { useBookmarksStore } from '@/store/bookmarks';
import BookmarkItem from './BookmarkItem.vue';

// Define all events that this component can emit upwards
const emit = defineEmits(['edit-bookmark', 'filter-by-tag', 'delete-bookmark']);

const bookmarksStore = useBookmarksStore();

// Use a computed property to reactively get bookmarks from the store
const bookmarks = computed(() => bookmarksStore.bookmarks);

onMounted(() => {
  // Fetch bookmarks if they haven't been loaded yet or if a refresh is needed
  // Consider if this should always fetch or only if state.bookmarks is empty
  if (bookmarksStore.bookmarks.length === 0) {
    bookmarksStore.fetchBookmarks();
  }
});

const handleEdit = (bookmark) => {
  emit('edit-bookmark', bookmark); // Emit the event with the bookmark data
};

const handleDelete = (bookmarkId) => {
  // Emit the event to the parent (HomeView) to handle the confirmation and deletion logic
  emit('delete-bookmark', bookmarkId);
};

const handleTagClick = (tag) => {
  emit('filter-by-tag', tag); // Emit the event with the tag
};

</script>

<style scoped>
.bookmark-list-container {
  margin-top: 20px;
}

.loading-message, .error-message, .empty-message {
  text-align: center;
  padding: 20px;
  font-size: 1.1em;
  color: #555;
}

.error-message {
  color: red;
}

.bookmarks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); /* Responsive grid */
  gap: 20px;
}

/* For single column on smaller screens, if not already handled by minmax */
@media (max-width: 600px) {
  .bookmarks-grid {
    grid-template-columns: 1fr;
  }
}
</style>
