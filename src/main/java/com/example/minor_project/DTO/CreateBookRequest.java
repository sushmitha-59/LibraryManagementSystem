package com.example.minor_project.DTO;

import com.example.minor_project.model.Author;
import com.example.minor_project.model.Book;
import com.example.minor_project.model.genre_enum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CreateBookRequest {
    @NotBlank
    @Column(unique = true,nullable = false)
    private String name;
    @NotNull
    private genre_enum genre;
    @NotNull
    private Integer price;
    @NotBlank
    private String author_name;
    @NotBlank
    private String author_email;

    public Book to(){
        //here we have to build both book objects and  author objects , so book can take the author object
        Author author=Author.builder()
                .name(this.author_name)
                .email(this.author_email)
                .build();

        return Book.builder()
                .name(this.name)
                .genre(this.genre)
                .price(this.price)
                .author(author)
                .build();
    }
}
