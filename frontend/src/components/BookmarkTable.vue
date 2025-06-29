<template>
  <div class="bookmark-table-container">
    <div v-if="bookmarksStore.isLoading" class="loading-message">Loading bookmarks...</div>
    <div v-if="bookmarksStore.error && !bookmarksStore.isLoading" class="error-message">
      Error loading bookmarks: {{ bookmarksStore.error }}
    </div>
    <div v-if="!bookmarksStore.isLoading && !bookmarksStore.error && bookmarks.length === 0" class="empty-message">
      No bookmarks found.
    </div>

    <table v-if="!bookmarksStore.isLoading && !bookmarksStore.error && bookmarks.length > 0" class="bookmarks-table">
      <thead>
        <tr>
          <th>Title</th>
          <th>Link</th>
          <th>Tags</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="bookmark in bookmarks" :key="bookmark.id">
          <td data-label="Title">{{ bookmark.title || 'Untitled' }}</td>
          <td data-label="Link">
            <a :href="bookmark.url" target="_blank" rel="noopener noreferrer">{{ bookmark.url }}</a>
          </td>
          <td data-label="Tags">
            <span v-if="bookmark.tags && bookmark.tags.length">
              <span v-for="(tag, index) in bookmark.tags" :key="tag" class="tag" @click="emitTagClick(tag)">
                {{ tag }}<span v-if="index < bookmark.tags.length - 1">, </span>
              </span>
            </span>
            <span v-else>-</span>
          </td>
          <td data-label="Actions" class="actions-cell">
            <button @click="emitEdit(bookmark)" class="action-button edit-button">Edit</button>
            <button @click="emitDelete(bookmark.id)" class="action-button delete-button">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { computed, defineEmits } from 'vue';
import { useBookmarksStore } from '@/store/bookmarks';

const bookmarksStore = useBookmarksStore();
const bookmarks = computed(() => bookmarksStore.bookmarks);

const emit = defineEmits(['edit-bookmark', 'delete-bookmark', 'filter-by-tag']);

const emitEdit = (bookmark) => {
  emit('edit-bookmark', bookmark);
};

const emitDelete = (bookmarkId) => {
  emit('delete-bookmark', bookmarkId); // This will be handled by parent (e.g., HomeView or a list manager)
};

const emitTagClick = (tag) => {
  emit('filter-by-tag', tag);
};

</script>

<style scoped>
.bookmark-table-container {
  margin-top: 1rem; /* Consistent spacing */
  overflow-x: auto;
}

/* .loading-message, .error-message, .empty-message are global */

.bookmarks-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  font-size: 0.9rem;
  background-color: var(--white);
  box-shadow: var(--box-shadow);
  border-radius: var(--border-radius); /* Apply to container if table itself can't have it with overflow */
  overflow: hidden; /* Ensures border-radius clips content */
}

.bookmarks-table th, .bookmarks-table td {
  border: 1px solid var(--medium-gray);
  padding: 0.75rem; /* 12px */
  text-align: left;
  vertical-align: middle; /* Better for mixed content cells */
}

.bookmarks-table th {
  background-color: var(--light-gray);
  font-weight: 500; /* Medium weight for headers */
  color: var(--text-color);
  white-space: nowrap;
}

.bookmarks-table tr:nth-child(even) {
  background-color: var(--light-gray); /* Subtle striping for even rows */
}
.bookmarks-table tr:nth-child(odd) {
  background-color: var(--white);
}


.bookmarks-table tr:hover {
  background-color: #e9ecef; /* Slightly darker hover */
}

.bookmarks-table td a {
  /* color: var(--primary-color); from global */
  word-break: break-all;
}
/* .bookmarks-table td a:hover is global */

.tag {
  display: inline-block;
  background-color: var(--medium-gray);
  color: var(--text-color-muted);
  padding: 0.2rem 0.5rem; /* Smaller padding for table tags */
  border-radius: var(--border-radius);
  font-size: 0.8rem; /* Slightly smaller for table view */
  cursor: pointer;
  transition: background-color 0.2s ease, color 0.2s ease;
}
.tag:hover {
  background-color: var(--primary-color);
  color: var(--white);
}
.tag:not(:last-child) {
    margin-right: 0.25rem;
}


.actions-cell {
  white-space: nowrap;
  text-align: right; /* Align buttons to the right */
}

.action-button {
  /* padding: 0.3rem 0.6rem; /* Smaller buttons for table view */
  /* font-size: 0.8rem; */
  /* margin-right: 0.25rem; */
  /* Global button styles are fine, component specific can override if needed */
}
/* .action-button:last-child from global */

.edit-button {
  background-color: var(--warning-color);
  color: var(--text-color);
  border-color: var(--warning-color);
}
.edit-button:hover {
  background-color: #e0a800;
  border-color: #e0a800;
}

.delete-button {
  background-color: var(--danger-color);
  color: var(--white);
  border-color: var(--danger-color);
}
.delete-button:hover {
  background-color: #c82333;
  border-color: #c82333;
}

/* Responsive adjustments for table */
@media screen and (max-width: 768px) {
  .bookmarks-table {
    box-shadow: none; /* Remove shadow for stacked view if it looks odd */
    border-radius: 0;
  }
  .bookmarks-table thead {
    display: none;
  }
  .bookmarks-table tr {
    display: block;
    margin-bottom: 1rem;
    border: 1px solid var(--medium-gray);
    border-radius: var(--border-radius);
    box-shadow: var(--box-shadow);
    background-color: var(--white) !important; /* Override striping */
  }
  .bookmarks-table td {
    display: block;
    text-align: right;
    padding-left: 45%; /* Adjust for label */
    position: relative;
    border: none;
    border-bottom: 1px solid var(--medium-gray);
    min-height: 2.5rem; /* Ensure cells have some height */
    display: flex; /* For vertical centering of content */
    align-items: center;
    justify-content: flex-end; /* Align content to the right */
  }
  .bookmarks-table td:last-child {
    border-bottom: none;
  }
  .bookmarks-table td::before {
    content: attr(data-label);
    position: absolute;
    left: 0.75rem; /* padding of td */
    width: calc(45% - 1.5rem); /* Adjust width */
    padding-right: 0.75rem;
    white-space: nowrap;
    text-align: left;
    font-weight: 500;
    color: var(--text-color);
  }
  .actions-cell {
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
  }
}
</style>
