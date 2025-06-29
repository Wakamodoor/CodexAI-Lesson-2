import { defineStore } from 'pinia'
import axios from 'axios' // For API calls

// It's good practice to define your API base URL in one place
const API_BASE_URL = '/api/bookmarks'; // Vite proxy will handle this

export const useBookmarksStore = defineStore('bookmarks', {
  state: () => ({
    bookmarks: [],
    isLoading: false,
    error: null,
  }),
  getters: {
    // Example getter:
    // totalBookmarks: (state) => state.bookmarks.length,
  },
  actions: {
    async fetchBookmarks() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.get(API_BASE_URL);
        this.bookmarks = response.data;
      } catch (err) {
        this.error = err.message || 'Failed to fetch bookmarks';
        console.error("Error fetching bookmarks:", err);
      } finally {
        this.isLoading = false;
      }
    },
    // Placeholder for addBookmark
    async addBookmark(newBookmark) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.post(API_BASE_URL, newBookmark);
        // Add to local state or refetch
        this.bookmarks.push(response.data); // Assuming API returns the created bookmark
      } catch (err) {
        this.error = err.response?.data?.message || err.message || 'Failed to add bookmark';
        console.error("Error adding bookmark:", err);
        // throw err; // Optionally re-throw to handle in component
      } finally {
        this.isLoading = false;
      }
    },
    // Placeholder for updateBookmark
    async updateBookmark(id, updatedBookmark) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, updatedBookmark);
        const index = this.bookmarks.findIndex(b => b.id === id);
        if (index !== -1) {
          this.bookmarks[index] = response.data; // Assuming API returns the updated bookmark
        }
      } catch (err) {
        this.error = err.response?.data?.message || err.message || 'Failed to update bookmark';
        console.error("Error updating bookmark:", err);
      } finally {
        this.isLoading = false;
      }
    },
    // Placeholder for deleteBookmark
    async deleteBookmark(id) {
      this.isLoading = true;
      this.error = null;
      try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        this.bookmarks = this.bookmarks.filter(b => b.id !== id);
      } catch (err) {
        this.error = err.message || 'Failed to delete bookmark';
        console.error("Error deleting bookmark:", err);
      } finally {
        this.isLoading = false;
      }
    },
    // Placeholder for searchBookmarks
    async searchBookmarks(keyword) {
        this.isLoading = true;
        this.error = null;
        try {
            const response = await axios.get(`${API_BASE_URL}/search`, { params: { keyword } });
            this.bookmarks = response.data;
        } catch (err) {
            this.error = err.message || 'Failed to search bookmarks';
            console.error("Error searching bookmarks:", err);
        } finally {
            this.isLoading = false;
        }
    },
    // Placeholder for filterBookmarksByTag
    async filterBookmarksByTag(tag) {
        this.isLoading = true;
        this.error = null;
        try {
            const response = await axios.get(`${API_BASE_URL}/filter`, { params: { tag } });
            this.bookmarks = response.data;
        } catch (err) {
            this.error = err.message || 'Failed to filter bookmarks';
            console.error("Error filtering bookmarks:", err);
        } finally {
            this.isLoading = false;
        }
    }
  }
})
