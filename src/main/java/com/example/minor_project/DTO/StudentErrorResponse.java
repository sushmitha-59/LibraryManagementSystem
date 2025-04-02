package com.example.minor_project.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentErrorResponse {
    private String ErrorResponse;

    public StudentErrorResponse(String error) {
        ErrorResponse = error;
    }
}
