<template>
  <div class="search-filter-controls">
    <div class="search-bar">
      <input
        type="search"
        v-model.trim="searchKeyword"
        placeholder="Search by title or description..."
        @keyup.enter="performSearch"
      />
      <button @click="performSearch" :disabled="bookmarksStore.isLoading">Search</button>
      <button @click="clearSearchAndFilter" v-if="isActiveSearchOrFilter" :disabled="bookmarksStore.isLoading">Clear</button>
    </div>
    <div v-if="activeFilterTag" class="active-filter-display">
      Filtering by tag: <strong>{{ activeFilterTag }}</strong>
      (<a href="#" @click.prevent="clearSearchAndFilter">Clear Filter</a>)
    </div>
     <div v-if="activeSearchKeyword && !activeFilterTag" class="active-filter-display">
      Searching for: <strong>{{ activeSearchKeyword }}</strong>
      (<a href="#" @click.prevent="clearSearchAndFilter">Clear Search</a>)
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useBookmarksStore } from '@/store/bookmarks';

const bookmarksStore = useBookmarksStore();
const searchKeyword = ref('');

// Reactive state to track active filters/searches for UI feedback
const activeFilterTag = ref(''); // Store the currently active filter tag
const activeSearchKeyword = ref(''); // Store the currently active search keyword

const isActiveSearchOrFilter = computed(() => activeFilterTag.value !== '' || activeSearchKeyword.value !== '');

const performSearch = async () => {
  if (!searchKeyword.value) {
    // If search keyword is empty, fetch all bookmarks (clear search)
    await bookmarksStore.fetchBookmarks();
    activeSearchKeyword.value = '';
    activeFilterTag.value = ''; // Clear tag filter as well
    return;
  }
  activeFilterTag.value = ''; // Clear any active tag filter
  await bookmarksStore.searchBookmarks(searchKeyword.value);
  activeSearchKeyword.value = searchKeyword.value; // Store the keyword that was searched
  // searchKeyword.value = ''; // Optionally clear input after search
};

const filterByTag = async (tag) => {
  searchKeyword.value = ''; // Clear search input
  activeSearchKeyword.value = ''; // Clear any active search
  await bookmarksStore.filterBookmarksByTag(tag);
  activeFilterTag.value = tag; // Store the tag that was filtered by
};

const clearSearchAndFilter = async () => {
  searchKeyword.value = '';
  activeFilterTag.value = '';
  activeSearchKeyword.value = '';
  await bookmarksStore.fetchBookmarks(); // Fetch all bookmarks
};

// Expose methods to be called from parent if needed (e.g., from HomeView)
defineExpose({
  filterByTag,
  clearSearchAndFilter
});

</script>

<style scoped>
.search-filter-controls {
  margin-bottom: 1.5rem; /* Consistent spacing */
  padding: 1rem;
  background-color: var(--light-gray);
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
}

.search-bar {
  display: flex;
  gap: 0.75rem; /* 12px */
  align-items: center;
}

.search-bar input[type="search"] {
  flex-grow: 1;
  padding: 0.625rem; /* 10px */
  border: 1px solid var(--dark-gray);
  border-radius: var(--border-radius);
  font-size: 0.95rem;
  color: var(--text-color);
  background-color: var(--white);
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.search-bar input[type="search"]:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
  outline: none;
}


.search-bar button {
  /* Uses global button styles, specific overrides below */
  padding: 0.625rem 1rem; /* 10px 16px */
  font-weight: 500;
}

.search-bar button:not([v-if]) { /* Search button */
  background-color: var(--primary-color);
  color: var(--white);
  border-color: var(--primary-color);
}
.search-bar button:not([v-if]):hover {
  background-color: var(--primary-color-dark);
  border-color: var(--primary-color-dark);
}

/* Clear button specific style */
.search-bar button[v-if] {
  background-color: var(--secondary-color);
  color: var(--white);
  border-color: var(--secondary-color);
}
.search-bar button[v-if]:hover {
  background-color: var(--secondary-color-dark);
  border-color: var(--secondary-color-dark);
}

.search-bar button:disabled {
  background-color: var(--medium-gray);
  border-color: var(--medium-gray);
  color: var(--text-color-muted);
  cursor: not-allowed;
}

.active-filter-display {
  margin-top: 0.75rem;
  font-size: 0.9rem;
  color: var(--text-color-muted);
  padding: 0.5rem 0.75rem; /* 8px 12px */
  background-color: var(--medium-gray);
  border-radius: var(--border-radius);
  display: inline-block;
}
.active-filter-display strong {
  color: var(--text-color);
  font-weight: 500;
}
.active-filter-display a {
    color: var(--primary-color);
    text-decoration: underline;
    margin-left: 0.375rem; /* 6px */
}
.active-filter-display a:hover {
    color: var(--primary-color-dark);
}

@media (max-width: 600px) {
  .search-bar {
    flex-direction: column;
    align-items: stretch; /* Make input and buttons full width */
  }
  .search-bar input[type="search"] {
    margin-bottom: 0.5rem; /* Space when stacked */
  }
  .search-bar button {
    width: 100%;
  }
  .active-filter-display {
    display: block; /* Full width on small screens */
    text-align: center;
  }
}
</style>
