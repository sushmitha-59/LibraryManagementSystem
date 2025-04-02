package com.example.minor_project.Repository;

import com.example.minor_project.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {
    Author findByEmail(String email);
}
