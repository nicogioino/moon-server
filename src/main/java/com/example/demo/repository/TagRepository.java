package com.example.demo.repository;


import com.example.demo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT u.name FROM Tag u ")
    Optional<String[]> getAllTagsNames();
    @Query("SELECT u FROM Tag u WHERE u.name in :tags")
    ArrayList<Tag> getTagsByName(String[] tags);
}
