package com.example.bookmarkmanager.service;

import com.example.bookmarkmanager.model.Bookmark;
import com.example.bookmarkmanager.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for @Transactional

import javax.persistence.EntityNotFoundException; // Standard JPA exception
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    @Transactional // Ensure this operation is atomic
    public Bookmark createBookmark(Bookmark bookmark) {
        // Validation for URL format is handled by @Valid and @URL annotations at the controller and entity level.
        // Additional business logic specific to creation can be added here if needed.
        return bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional(readOnly = true) // Optimize for read operations
    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bookmark> getBookmarkById(Long id) {
        return bookmarkRepository.findById(id);
    }

    @Override
    @Transactional
    public Bookmark updateBookmark(Long id, Bookmark bookmarkDetails) {
        Bookmark existingBookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bookmark not found with id: " + id));

        existingBookmark.setUrl(bookmarkDetails.getUrl());
        existingBookmark.setTitle(bookmarkDetails.getTitle());
        existingBookmark.setDescription(bookmarkDetails.getDescription());
        existingBookmark.setTags(bookmarkDetails.getTags());
        // @Valid on controller will re-validate the bookmarkDetails
        return bookmarkRepository.save(existingBookmark);
    }

    @Override
    @Transactional
    public void deleteBookmark(Long id) {
        if (!bookmarkRepository.existsById(id)) {
            throw new EntityNotFoundException("Bookmark not found with id: " + id);
        }
        bookmarkRepository.deleteById(id);
    }

    // Implementation for search and filter will be done in the next step
    @Override
    @Transactional(readOnly = true)
    public List<Bookmark> searchBookmarksByKeyword(String keyword) {
        // To be implemented in the next plan step
        return bookmarkRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bookmark> filterBookmarksByTag(String tag) {
        // To be implemented in the next plan step
        return bookmarkRepository.findByTagsContainingIgnoreCase(tag);
    }
}
