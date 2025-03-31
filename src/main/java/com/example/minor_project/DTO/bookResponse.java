package com.example.minor_project.DTO;

import com.example.minor_project.model.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Integer studentId;
    private String authorName;
    private Integer authorId;
    private Map<String,String> transactions;
}
