package com.example.minor_project.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor @Builder
//generic class , searchKey can be genre, name, id , author , searchValue will be associate values
//(genre,CSE),(author,bjarne),(id,1),(name,cpp)
public class getBookRequest {
    @NotBlank
    private String searchKey;
    @NotBlank
    private String searchValue;
}
