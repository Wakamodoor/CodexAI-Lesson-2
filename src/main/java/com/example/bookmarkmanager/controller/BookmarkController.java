package com.example.bookmarkmanager.controller;

import com.example.bookmarkmanager.model.Bookmark;
import com.example.bookmarkmanager.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException; // For handling not found cases
import javax.validation.Valid;
import java.util.List;
import java.util.Optional; // For handling optional results

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // Create a new bookmark
    @PostMapping
    public ResponseEntity<Bookmark> createBookmark(@Valid @RequestBody Bookmark bookmark) {
        Bookmark createdBookmark = bookmarkService.createBookmark(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

    // Get all bookmarks
    @GetMapping
    public ResponseEntity<List<Bookmark>> getAllBookmarks() {
        List<Bookmark> bookmarks = bookmarkService.getAllBookmarks();
        return ResponseEntity.ok(bookmarks);
    }

    // Get a single bookmark by ID
    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable Long id) {
        Optional<Bookmark> bookmark = bookmarkService.getBookmarkById(id);
        return bookmark.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing bookmark
    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> updateBookmark(@PathVariable Long id, @Valid @RequestBody Bookmark bookmarkDetails) {
        try {
            Bookmark updatedBookmark = bookmarkService.updateBookmark(id, bookmarkDetails);
            return ResponseEntity.ok(updatedBookmark);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a bookmark
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        try {
            bookmarkService.deleteBookmark(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Or some other appropriate error
        }
    }

    // Search bookmarks by keyword (in title or description)
    // Implementation will be refined in the next step
    @GetMapping("/search")
    public ResponseEntity<List<Bookmark>> searchBookmarks(@RequestParam String keyword) {
        List<Bookmark> bookmarks = bookmarkService.searchBookmarksByKeyword(keyword);
        return ResponseEntity.ok(bookmarks);
    }

    // Filter bookmarks by tag
    // Implementation will be refined in the next step
    @GetMapping("/filter")
    public ResponseEntity<List<Bookmark>> filterBookmarksByTag(@RequestParam String tag) {
        List<Bookmark> bookmarks = bookmarkService.filterBookmarksByTag(tag);
        return ResponseEntity.ok(bookmarks);
    }
}
