package com.example.minor_project.Service;

import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Admin;
import com.example.minor_project.Repository.AdminRepo;
import com.example.minor_project.model.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class adminService {
    @Autowired
    public AdminRepo adminrepo;
    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    public Admin CreateAdmin(Admin admin) throws Exception {
        Users user=admin.getUser();
        customUserDetailsService.SaveUser(Constants.ADMIN_USER,user);
        return adminrepo.save(admin);
    }

    public List<Admin> getAllAdmin() {
        return adminrepo.findAll();
    }

    public Admin getAdminByIdEmail(@NotBlank String searchKey, @NotBlank String searchValue) throws Exception{
        switch (searchKey) {
            case "id": {
                Optional<Admin> admin = adminrepo.findById(Integer.parseInt(searchValue));
                if (admin.isEmpty()) {
                    throw new Exception("Cannot find Admin with SearchKey " + searchKey + " and searchValue "+ searchValue);
                }
                return admin.get();
            }
            case "email":
                Admin admin = adminrepo.findByEmail(searchValue);
                if (admin.getId() ==null) {
                    throw new Exception("Cannot find Admin with SearchKey " + searchKey + " and searchValue "+ searchValue);
                }
                return admin;
            default:
                throw new Exception("Invalid SearchKey.");
        }
    }
}
