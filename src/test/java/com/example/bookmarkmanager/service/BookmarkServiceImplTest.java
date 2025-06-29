package com.example.bookmarkmanager.service;

import com.example.bookmarkmanager.model.Bookmark;
import com.example.bookmarkmanager.repository.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    private Bookmark bookmark1;
    private Bookmark bookmark2;

    @BeforeEach
    void setUp() {
        bookmark1 = new Bookmark(1L, "http://example.com", "Example", "An example bookmark", new ArrayList<>(Arrays.asList("test", "example")));
        bookmark2 = new Bookmark(2L, "http://anotherexample.com", "Another", "Another one", new ArrayList<>(Arrays.asList("test", "another")));
    }

    @Test
    void createBookmark_shouldSaveAndReturnBookmark() {
        // Arrange
        Bookmark newBookmark = new Bookmark(null, "http://new.com", "New", "New desc", new ArrayList<>(List.of("new")));
        Bookmark savedBookmark = new Bookmark(3L, "http://new.com", "New", "New desc", new ArrayList<>(List.of("new")));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(savedBookmark);

        // Act
        Bookmark result = bookmarkService.createBookmark(newBookmark);

        // Assert
        assertNotNull(result);
        assertEquals(savedBookmark.getId(), result.getId());
        assertEquals(newBookmark.getUrl(), result.getUrl());
        verify(bookmarkRepository, times(1)).save(newBookmark);
    }

    @Test
    void getAllBookmarks_shouldReturnListOfBookmarks() {
        // Arrange
        when(bookmarkRepository.findAll()).thenReturn(Arrays.asList(bookmark1, bookmark2));

        // Act
        List<Bookmark> results = bookmarkService.getAllBookmarks();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(bookmark1.getTitle(), results.get(0).getTitle());
        verify(bookmarkRepository, times(1)).findAll();
    }

    @Test
    void getBookmarkById_whenExists_shouldReturnBookmark() {
        // Arrange
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.of(bookmark1));

        // Act
        Optional<Bookmark> result = bookmarkService.getBookmarkById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bookmark1.getTitle(), result.get().getTitle());
        verify(bookmarkRepository, times(1)).findById(1L);
    }

    @Test
    void getBookmarkById_whenNotExists_shouldReturnEmptyOptional() {
        // Arrange
        when(bookmarkRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Bookmark> result = bookmarkService.getBookmarkById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(bookmarkRepository, times(1)).findById(3L);
    }

    // Add more tests for update, delete, search, filter, and edge cases (e.g., null inputs if applicable)
}
