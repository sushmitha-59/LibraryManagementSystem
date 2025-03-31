package com.example.minor_project.DTO;
import lombok.*;
import java.util.List;

@Builder @Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class SearchStudentResponse {
    List<StudentResponse> studentResponseList;
}
