package com.example.minor_project.DTO;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StudentResponse implements Serializable {
    private Integer id;
    private Map<Integer, String> books;
    private Map<String, String> transactions;
    private Integer age;
    private String name;
    private String roll_number;
    private String email;
    private Date CreatedOn;
    private Date UpdatedOn;
    private Integer userId;
    private String message;
}
