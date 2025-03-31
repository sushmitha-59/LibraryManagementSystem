package com.example.minor_project.Controller;

import com.example.minor_project.Service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/csv")
public class CSVImport {
    @Autowired
    CSVService csvService;
    @PostMapping("/upload")
    public ResponseEntity<String> csvUpload(@RequestParam("file")MultipartFile file , @RequestParam("entity") String entity ){
        try{
            Integer count=csvService.parseCSVAndSaveData(file.getInputStream(),entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("CSV Files parsed and uploaded " + count + " records successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to parse the CSV " + e.getMessage().toString());
        }
    }
}
