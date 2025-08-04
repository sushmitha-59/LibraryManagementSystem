package com.example.minor_project.Controller;

import com.example.minor_project.DTO.AdminResponse;
import com.example.minor_project.DTO.CreateAdminRequest;
import com.example.minor_project.DTO.GetAdminRequest;
import com.example.minor_project.Service.adminService;
import com.example.minor_project.model.Admin;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    public adminService Adminservice;

    //creation of admin should be done by admins,only
    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody @Valid CreateAdminRequest create_admin_dto) {
        try {
            Adminservice.CreateAdmin(create_admin_dto.to());
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin got created/updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause()!=null?e.getCause().getLocalizedMessage(): e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAdmins() {
        List<AdminResponse> adminResponses = new ArrayList<>();
        try {
            List<Admin> admins = Adminservice.getAllAdmin();
            for (Admin admin : admins) {
                adminResponses.add(admin.to());
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(adminResponses);
        } catch (Exception e) {
            //log.error("Admin get All requests failure : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause()!=null?e.getCause().getLocalizedMessage(): e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAdmin(@RequestBody GetAdminRequest getAdminRequest) {
        try {
            Admin admin = Adminservice.getAdminByIdEmail(getAdminRequest.getSearchKey(), getAdminRequest.getSearchValue());
            return ResponseEntity.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON)
                    .body(admin.to());
        } catch (Exception e) {
            String msg=e.getCause()!=null?e.getCause().getLocalizedMessage(): e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin get request failed : " + msg );
        }
    }
}
