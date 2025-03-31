package com.example.minor_project.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class SearchStudentRequest {
    @NotBlank(message = "searchKey should not be empty")
    private String searchKey;
    @NotBlank(message = "searchValue should not be empty")
    private String searchValue;
}
