package com.example.minor_project.Service;

import com.example.minor_project.Repository.AuthorRepo;
import com.example.minor_project.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    AuthorRepo author_repo;

    public Author getOrCreateAuthor(Author author) {
        //first we should check if the author exists in the database
        Author existingAuthor = author_repo.findByEmail(author.getEmail());

        //if it exists , we should return the existingAuthor else create new one
        if (existingAuthor == null) {
            existingAuthor = author_repo.save(author); //no existingAuthor , so creating new one
        }
        return existingAuthor;
    }
}
