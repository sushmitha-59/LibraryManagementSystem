package com.example.minor_project.DTO;

import com.example.minor_project.model.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor @Builder
public class bookResponse {
    private Integer id;
    private Date createdOn;
    private String name;
    private Date updatedOn;
    private genre_enum genre;
    private Integer price;
    private Student student;
    private String authorName;
    private Integer authorId;
    private List<Transaction> transactions;
}
