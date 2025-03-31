package com.example.minor_project.Repository;

import com.example.minor_project.model.Admin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {

    Admin findByEmail(@NotBlank String searchValue);
}
