package com.example.minor_project.Controller;

import com.example.minor_project.DTO.CreateBookRequest;
import com.example.minor_project.DTO.SearchBook_response;
import com.example.minor_project.DTO.bookResponse;
import com.example.minor_project.DTO.getBookRequest;
import com.example.minor_project.Service.BookService;
import com.example.minor_project.model.Book;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService book_service;

    @PostMapping("/create")
    public ResponseEntity<String> createBook(@RequestBody @Valid CreateBookRequest create_book_dto) {
        Book book = book_service.CreateOrUpdateBook(create_book_dto.to());
        return ResponseEntity.ok("Book got added successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<?> GetBook(@RequestBody @Valid getBookRequest getBook_dto) {
        //first get the book_list from the service
        try {
            List<Book> books = book_service.getBook(getBook_dto.getSearchKey(), getBook_dto.getSearchValue());
            //then build the response_object
            List<bookResponse> bookResponseList = new ArrayList<>();
            for (Book book : books) {
                bookResponseList.add(book.to());
            }
            return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON)
                    .body(new SearchBook_response(bookResponseList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public SearchBook_response GetAllBook() {
        List<bookResponse> book_resp = book_service.getAllBooks();
        return new SearchBook_response(book_resp);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete_book(@PathVariable Integer id) {
        String str = book_service.deleteBook(id);
        if (str.equals("Book got deleted successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body("Book got deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
    }
}
