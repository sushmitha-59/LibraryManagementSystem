package com.example.minor_project.DTO;

import com.example.minor_project.model.Book;
import com.example.minor_project.model.Transaction;
import lombok.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder @ToString
public class StudentResponse {
    private Integer id;
    private List<Book> books;
    private List<Transaction> transactions;
    private Integer age;
    private String name;
    private String roll_number;
    private String email;
    private Date CreatedOn;
    private Date UpdatedOn;
    private String message;
}
