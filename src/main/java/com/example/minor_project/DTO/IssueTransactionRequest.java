package com.example.minor_project.DTO;

import com.example.minor_project.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueTransactionRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String bookId;
    @NotBlank
    private String adminId;
    @NotNull
    private TransactionType transactionType;
}
