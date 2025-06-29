package com.example.bookmarkmanager.service;

import com.example.bookmarkmanager.model.Bookmark;
import java.util.List;
import java.util.Optional;

public interface BookmarkService {
    Bookmark createBookmark(Bookmark bookmark);
    List<Bookmark> getAllBookmarks();
    Optional<Bookmark> getBookmarkById(Long id);
    Bookmark updateBookmark(Long id, Bookmark bookmarkDetails);
    void deleteBookmark(Long id);
    List<Bookmark> searchBookmarksByKeyword(String keyword);
    List<Bookmark> filterBookmarksByTag(String tag);
}
