<template>
  <div class="home">
    <AddBookmarkForm @bookmark-added="handleAddBookmark" />
    <hr class="separator" />
    <SearchAndFilterControls ref="searchControls" @cleared="refreshBookmarks" />

    <div class="view-controls">
      <h2>My Bookmarks</h2>
      <div class="view-switcher">
        <button @click="setViewMode('card')" :class="{ active: currentViewMode === 'card' }">
          Card View
        </button>
        <button @click="setViewMode('table')" :class="{ active: currentViewMode === 'table' }">
          Table View
        </button>
      </div>
    </div>

    <BookmarkList
      v-if="currentViewMode === 'card'"
      @edit-bookmark="openEditModal"
      @filter-by-tag="handleFilterByTag"
      @delete-bookmark="confirmDeleteBookmark"
    />
    <BookmarkTable
      v-if="currentViewMode === 'table'"
      @edit-bookmark="openEditModal"
      @filter-by-tag="handleFilterByTag"
      @delete-bookmark="confirmDeleteBookmark"
    />

    <EditBookmarkModal
      :is-visible="isEditModalVisible"
      :bookmark-to-edit="selectedBookmarkForEdit"
      @close="closeEditModal"
      @bookmark-updated="handleBookmarkUpdated"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import AddBookmarkForm from '@/components/AddBookmarkForm.vue';
import BookmarkList from '@/components/BookmarkList.vue';
import BookmarkTable from '@/components/BookmarkTable.vue'; // Import BookmarkTable
import EditBookmarkModal from '@/components/EditBookmarkModal.vue';
import SearchAndFilterControls from '@/components/SearchAndFilterControls.vue';
import { useBookmarksStore } from '@/store/bookmarks';

const bookmarksStore = useBookmarksStore();
const searchControls = ref(null);

const currentViewMode = ref('card'); // 'card' or 'table'
const isEditModalVisible = ref(false);
const selectedBookmarkForEdit = ref(null);

const setViewMode = (mode) => {
  currentViewMode.value = mode;
};

const openEditModal = (bookmark) => {
  selectedBookmarkForEdit.value = bookmark;
  isEditModalVisible.value = true;
};

const closeEditModal = () => {
  isEditModalVisible.value = false;
  selectedBookmarkForEdit.value = null;
};

const handleBookmarkUpdated = () => {
  refreshBookmarks(); // Refreshes data, modal might close itself or be closed here
};

const handleAddBookmark = () => {
  if (searchControls.value) {
    searchControls.value.clearSearchAndFilter();
  } else {
    refreshBookmarks();
  }
  // Optionally switch to card view or the view where the new item is most visible
  // For now, keeps current view
};

const handleFilterByTag = (tag) => {
  if (searchControls.value) {
    searchControls.value.filterByTag(tag);
  }
};

const confirmDeleteBookmark = async (bookmarkId) => {
  if (confirm('Are you sure you want to delete this bookmark?')) {
    bookmarksStore.error = null;
    try {
      await bookmarksStore.deleteBookmark(bookmarkId);
      if (bookmarksStore.error) {
        alert(`Error deleting bookmark: ${bookmarksStore.error}`);
      }
      // List will update reactively due to store change.
      // No explicit refreshBookmarks() needed here if store handles local state update.
    } catch (error) {
      console.error("Error in confirmDeleteBookmark:", error);
      alert("An unexpected error occurred while deleting.");
    }
  }
};

const refreshBookmarks = () => {
  // This function might need to be smarter if filters/searches should persist across manual refreshes
  // For now, it just fetches all, assuming SearchAndFilterControls handles reapplying its state if needed.
  if (searchControls.value && (searchControls.value.activeSearchKeyword || searchControls.value.activeFilterTag)) {
    // If a search or filter is active, re-trigger it to refresh results
    if(searchControls.value.activeSearchKeyword) {
        searchControls.value.performSearch(); // Re-perform search with existing keyword
    } else if (searchControls.value.activeFilterTag) {
        searchControls.value.filterByTag(searchControls.value.activeFilterTag); // Re-perform filter
    }
  } else {
    bookmarksStore.fetchBookmarks(); // Default: fetch all
  }
};


onMounted(() => {
  if (bookmarksStore.bookmarks.length === 0 && !bookmarksStore.isLoading) {
    if (searchControls.value && (searchControls.value.activeSearchKeyword || searchControls.value.activeFilterTag)) {
      // Let SearchAndFilterControls handle its initial fetch if a filter/search might be active
    } else {
      bookmarksStore.fetchBookmarks();
    }
  }
});

</script>

<style scoped>
.home {
  padding: 1rem 0; /* Reduce top/bottom padding as #app has it */
}

/* .separator is global now */

.view-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem; /* Consistent spacing */
  padding-bottom: 0.75rem;
  border-bottom: 1px solid var(--medium-gray);
}

.view-controls h2 {
  margin: 0;
  text-align: left;
  font-size: 1.5rem; /* Consistent with other H2s if needed, or adjust */
  font-weight: 500;
}

.view-switcher button {
  padding: 0.5rem 1rem; /* 8px 16px */
  margin-left: 0.5rem; /* 8px */
  border: 1px solid var(--primary-color);
  background-color: var(--white);
  color: var(--primary-color);
  border-radius: var(--border-radius);
  font-weight: 500;
  /* transition is global */
}

.view-switcher button.active {
  background-color: var(--primary-color);
  color: var(--white);
  border-color: var(--primary-color);
}

.view-switcher button:hover:not(.active) {
  background-color: var(--medium-gray); /* Lighter hover for non-active */
  border-color: var(--primary-color-dark);
}

/* Global h2 style is fine, no need to override here unless specific */

@media (max-width: 600px) {
  .view-controls {
    flex-direction: column;
    align-items: stretch; /* Full width */
  }
  .view-controls h2 {
    text-align: center;
    margin-bottom: 0.75rem;
  }
  .view-switcher {
    display: flex;
    justify-content: center; /* Center buttons */
  }
  .view-switcher button {
    flex-grow: 1; /* Make buttons take equal space if desired */
    margin-left: 0.25rem;
    margin-right: 0.25rem;
  }
  .view-switcher button:first-child {
    margin-left: 0;
  }
   .view-switcher button:last-child {
    margin-right: 0;
  }
}
</style>
