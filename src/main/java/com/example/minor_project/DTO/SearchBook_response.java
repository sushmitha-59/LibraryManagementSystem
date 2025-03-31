package com.example.minor_project.DTO;
import lombok.*;
import java.util.List;

@NoArgsConstructor  @AllArgsConstructor
@Getter  @Setter @Builder
public class SearchBook_response {
    List<bookResponse> bookResponseList;
}
