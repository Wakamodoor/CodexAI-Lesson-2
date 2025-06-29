package com.example.bookmarkmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ElementCollection;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "URL cannot be empty")
    @URL(message = "URL should be valid")
    private String url;

    private String title;
    private String description;

    @ElementCollection // For storing a list of simple strings
    private List<String> tags;
}
