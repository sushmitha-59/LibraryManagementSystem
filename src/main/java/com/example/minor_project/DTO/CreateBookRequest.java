package com.example.minor_project.DTO;

import com.example.minor_project.model.Author;
import com.example.minor_project.model.Book;
import com.example.minor_project.model.genre_enum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {
    @NotBlank(message = "name cannot be empty.")
    @Column(unique = true, nullable = false)
    private String name;
    @NotNull(message = "genre cannot be empty.")
    private genre_enum genre;
    private Integer price;
    @NotBlank(message = "author_name cannot be empty.")
    private String author_name;
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$"
    )
    @NotBlank(message = "Invalid email address.")
    private String author_email;

    public Book to() {
        //here we have to build both book objects and  author objects , so book can take the author object
        Author author = Author.builder()
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
