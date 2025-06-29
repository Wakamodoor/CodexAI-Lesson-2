package com.example.bookmarkmanager.repository;

import com.example.bookmarkmanager.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // Custom query methods for search and filter can be added here later
    List<Bookmark> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);
    List<Bookmark> findByTagsContainingIgnoreCase(String tag);
}
