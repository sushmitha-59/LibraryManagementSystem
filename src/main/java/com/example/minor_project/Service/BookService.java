package com.example.minor_project.Service;

import com.example.minor_project.DTO.bookResponse;
import com.example.minor_project.Repository.BookRepo;
import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Author;
import com.example.minor_project.model.Book;
import com.example.minor_project.model.genre_enum;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepo book_repo;
    @Autowired
    private AuthorService author_service;

    public Book CreateOrUpdateBook(Book book) {
        //while creating a book , first we have to check if the corresponding author exists , if not we should add him
        Author author = author_service.getOrCreateAuthor(book.getAuthor());
        book.setAuthor(author);
        return book_repo.save(book);
    }

    public List<Book> getBook(@NotBlank String searchKey, @NotBlank String searchValue) throws Exception {
        List<Book> books = new ArrayList<>();
        switch (searchKey) {
            case "name":
                try {
                    books = book_repo.findByName(searchValue);
                    return books;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case "genre":
                try {
                    books = book_repo.findByGenre(genre_enum.valueOf(searchValue));
                    return books;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case "authorName":
                try {
                    books = book_repo.findByAuthor_name(searchValue);
                    return books;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case "id": {
                Optional<Book> book = book_repo.findById(Integer.parseInt(searchValue));
                if (book.isEmpty()) {
                    throw new Exception("cannot find book with id :  " + searchValue);
                } else {
                    books.add(book.get());
                    return books;
                }
            }
            default:
                throw new Exception("Invalid Search key : " + searchKey);
        }
    }

    public String deleteBook(Integer id) {
        return book_repo.deleteBook(id);
    }

    public List<bookResponse> getAllBooks() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new Exception("USER is not authenticated.");
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<Book> books = book_repo.findAll();
            List<bookResponse> books2 = new ArrayList<>();
            List<String> authorities2 = authorities.stream()
                    .map((GrantedAuthority) -> GrantedAuthority.getAuthority())
                    .collect(Collectors.toUnmodifiableList());
            if (authorities2.contains(Constants.ADMIN_BOOKS_VIEW)) {
                System.out.println("admin view");
                for (Book book : books) {
                    books2.add(book.to());
                }
            } else {
                for (Book book : books) {
                    System.out.println("student view");
                    if (book.getStudent() == null) {
                        books2.add(book.to());
                    }
                }
            }
            return books2;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
