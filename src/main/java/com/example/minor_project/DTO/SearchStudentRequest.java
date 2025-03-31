package com.example.minor_project.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class SearchStudentRequest {
    @NotBlank
    private String searchKey;
    @NotBlank
    private String searchValue;
}
