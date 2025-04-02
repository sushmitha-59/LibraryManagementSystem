package com.example.minor_project.Controller;

import com.example.minor_project.DTO.IssueTransactionRequest;
import com.example.minor_project.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/initiate")
    public ResponseEntity<String> issueTransaction(@RequestBody @Valid IssueTransactionRequest issueTransactionRequest) {
        try {
            String TransactionId = transactionService.initiateTxn(issueTransactionRequest);
            return ResponseEntity.ok("TransactionId is : " + TransactionId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
